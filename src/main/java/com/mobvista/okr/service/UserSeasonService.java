package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.domain.*;
import com.mobvista.okr.dto.*;
import com.mobvista.okr.enums.*;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.repository.*;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.util.*;
import com.mobvista.okr.vm.RadarVM;
import com.mobvista.okr.vm.UserSeasonRadarVM;
import com.mobvista.okr.vm.XYAxisVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing UserSeason.
 *
 * @author 顾炜[GuWei]
 */
@Service
public class UserSeasonService {

    private final Logger log = LoggerFactory.getLogger(UserSeasonService.class);

    @Resource
    private UserSeasonRepository userSeasonRepository;
    @Resource
    private SeasonRepository seasonRepository;
    @Resource
    private UserProfileRepository userProfileRepository;
    @Resource
    private DepartmentRepository departmentRepository;
    @Resource
    private ScoreOptionRepository scoreOptionRepository;
    @Resource
    private UserSeasonItemRepository userSeasonItemRepository;
    @Resource
    private AssessTaskRepository assessTaskRepository;
    @Resource
    private AssessTaskItemRepository assessTaskItemRepository;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private UserReportDetailRepository userReportDetailRepository;
    @Resource
    private OkrContentRepository okrContentRepository;
    @Resource
    private ScoreOptionService scoreOptionService;
    @Resource
    private AdjusterService adjusterService;
    @Resource
    private UserReportRepository userReportRepository;

    /**
     * 我的季度考核列表
     *
     * @param pageable     分页
     * @param assessStatus 考核状态
     */
    public Page<UserSeasonDTO> myList(Pageable pageable, Byte assessStatus) {
        Long userId = SecurityUtils.getCurrentUserId();
        long count = userSeasonRepository.countByUserIdAndStatus(assessStatus, userId);
        List<UserSeasonDTO> userSeasonDTOs = new ArrayList<>();
        if (count > 0) {
            userSeasonDTOs = userSeasonRepository.queryByUserIdAndStatus(assessStatus, userId, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            for (UserSeasonDTO dto : userSeasonDTOs) {
                Boolean finished = dto.getAssessFinished();
                dto.setAssessStatus(null == finished || !finished ? AssessStatus.UNDERWAY.getCode() : dto.getAssessStatus());
                dto.setAssessStatusText(AssessStatus.getValueByCode(dto.getAssessStatus()));
                //设置考核进度状态
                ProgressStatusEnum processStatus = SeasonDateUtil.getProcessStatus(dto);
                dto.setProgressStatus(processStatus.getCode());
                dto.setProgressStatusText(processStatus.getName());
            }
        }
        Page<UserSeasonDTO> result = new PageImpl(userSeasonDTOs, pageable, count);
        return result;
    }


    /**
     * 判断用户对应状态的数据是否存在
     *
     * @param assessStatus 考核状态
     * @return
     */
    public boolean assessStatusDataIsExist(Byte assessStatus) {
        //检查入参是否正确
        ExceptionUtil.checkState(AssessStatus.check(assessStatus), "查询条件指定的状态不存在");
        long count = userSeasonRepository.countAssessStatusData(assessStatus, SecurityUtils.getCurrentUserId());
        return count > 0;
    }

    /**
     * 获取详情
     *
     * @param userSeasonId 用户考核id
     * @return
     */
    public UserSeasonDTO getDetail(Long userSeasonId) {
        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(userSeasonId);
        ExceptionUtil.checkState(userSeason != null, "用户季度考核不存在");

        Season season = seasonRepository.selectByPrimaryKey(userSeason.getSeasonId());
        UserSeasonDTO dto = new UserSeasonDTO(userSeason, season);
        //如果 当前季度考核未结束 & 非管理员角色，不能查看 分数和排名
        if (!season.isMakeSeasonScore()) {

            if (SecurityUtils.getUserOperatorPerm().getCanSeeSeasonDetailRankAndScore().equals(YesOrNoEnum.YES.getCode())) {
                BigDecimal score = BigDecimal.ZERO;
                UserSeasonItem item = userSeasonItemRepository.findOneByUserSeasonIdAndOptionId(userSeasonId, 0L);
                if (null != item) {
                    score = item.getScore();
                    dto.setScore(score);
                }
                score = null == score ? BigDecimal.ZERO : score;
                //查询大于当前考核的评分数量，并设置排名
                int count = userSeasonRepository.getUserSeasonRank(season.getId(), score);
                dto.setRanking(count);
            } else {
                dto.setScore(null);
                dto.setRanking(null);
            }
        } else {
            UserSeasonItem item = userSeasonItemRepository.findOneByUserSeasonIdAndOptionId(userSeasonId, 0L);
            if (null != item) {
                dto.setScore(item.getScore());
                dto.setRanking(item.getRanking());
            }
        }
        ProgressStatusEnum processStatus = SeasonDateUtil.getProcessStatus(dto);
        dto.setProgressStatus(processStatus.getCode());
        dto.setProgressStatusText(processStatus.getName());
        //设置得分的xy视图
        dto.setRankXYAxis(queryUserSeasonRankView(userSeason.getUserId()));
        dto.setScoreXYAxis(queryUserSeasonScoreView(userSeason.getUserId()));
        Map<Long, XYAxisVM> xyAxisVMMap = queryUserSeasonItemViewMap(Lists.newArrayList(userSeasonId));
        if (null != xyAxisVMMap) {
            dto.setScoreOptionItemXYAxis(xyAxisVMMap.get(userSeasonId));
        }

        //设置是否填写了OKR
        List<OkrContent> okrContentList = okrContentRepository.findAllByUserSeasonId(userSeasonId);
        if (okrContentList != null && okrContentList.size() != 0) {
            dto.setFinishContent(true);
        } else {
            dto.setFinishContent(false);
        }

        //设置已经选择的评价对象
        List<UserInfoDTO> selectedUsers = adjusterService.getSelectedUsers(userSeasonId, UserStatus.NORMAL.getCode());
        if (selectedUsers != null && selectedUsers.size() != 0) {
            dto.setAdjustCount((long) selectedUsers.size());
        }

        return dto;
    }


    /**
     * 获取个人信息
     *
     * @param userSeasonId 用户考核id
     * @return
     */
    public UserInfoDTO getDetailForUserInfo(Long userSeasonId) {
        //获取考核信息
        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(userSeasonId);
        ExceptionUtil.checkState(null != userSeason, "用户考核信息不存在");
        UserProfile userProfile = userProfileRepository.selectByPrimaryKey(userSeason.getUserId());
        ExceptionUtil.checkState(null != userProfile, "用户信息不存在");
        Department department = departmentRepository.selectByPrimaryKey(userProfile.getDepartmentId());

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(userProfile.getId());
        userInfoDTO.setRealName(userProfile.getRealName());
        userInfoDTO.setProfilePhoto(userProfile.getProfilePhoto());
        userInfoDTO.setEmail(userProfile.getEmail());
        userInfoDTO.setStatus(userProfile.getStatus());
        userInfoDTO.setJobName(userProfile.getJobName());
        userInfoDTO.setGender(userProfile.getGender());
        userInfoDTO.setRank(userProfile.getRank());
        userInfoDTO.setDepartmentId(userProfile.getDepartmentId());
        if (null != department) {
            userInfoDTO.setDepartmentName(department.getName());
        }
        if (null != userProfile.getLeaderId()) {
            userInfoDTO.setLeaderId(userProfile.getLeaderId());
            UserProfile leader = userProfileRepository.selectByPrimaryKey(userProfile.getLeaderId());
            if (null != leader) {
                userInfoDTO.setLeaderName(leader.getRealName());
            }
        }

        return userInfoDTO;
    }


    /**
     * 获取季度评分
     *
     * @param userSeasonId
     * @return
     */
    public List<UserScoreItemDTO> getDetailForUserScoreItem(Long userSeasonId) {
        //如果 当前季度考核未结束 & 非管理员角色，不能查看季度评分
        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(userSeasonId);
        ExceptionUtil.checkState(userSeason != null, "用户季度考核不存在");

        Season season = seasonRepository.selectByPrimaryKey(userSeason.getId());
        if (season != null && !season.isMakeSeasonScore()) {
            if (SecurityUtils.getUserOperatorPerm().getCanSeeUserSeasonDetailForUserScoreItem().equals(YesOrNoEnum.NO.getCode())) {
                return Lists.newArrayList();
            }
        }

        List<ScoreOption> scoreOptions = scoreOptionRepository.findAllByType(null);

        Map<Long, UserSeasonItem> userScoreItemMap = userSeasonItemRepository
                .findAllByUserSeasonIdIn(Lists.newArrayList(userSeasonId))
                .stream()
                .collect(Collectors.toMap(userScoreItem -> userScoreItem.getOptionId(), userScoreItem -> userScoreItem));

        if (userScoreItemMap.size() == 0) {
            return Lists.newArrayList();
        }

        return getUserScoreItemDTOS(userScoreItemMap, scoreOptions);
    }


    /**
     * 获取对此评价的列表
     *
     * @param userSeasonId
     * @return
     */
    public List<AssessScoreDTO> getDetailForAssessScoreList(Long userSeasonId) {
        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(userSeasonId);
        ExceptionUtil.checkState(userSeason != null, "用户季度考核不存在");

        List<AssessTask> assessTasks = assessTaskRepository.findAllByUserSeasonIdAndScoreIsNotNull(userSeasonId);
        if (null == assessTasks || assessTasks.size() == 0) {
            return new ArrayList<>();
        }
        Map<Long, UserProfile> userMap = null;
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<Long> assessorIdList = new ArrayList<>();
        List<Long> userSeasonIdList = new ArrayList<>();
        List<Long> assessTaskIdList = new ArrayList<>();

        assessTasks.forEach(assessTask -> {
            assessorIdList.add(assessTask.getAssessorId());
            userSeasonIdList.add(assessTask.getUserSeasonId());
            assessTaskIdList.add(assessTask.getId());
        });
        if (SecurityUtils.getUserOperatorPerm().getCanSeeUserDetailForAssessScoreList().equals(YesOrNoEnum.YES.getCode())) {

            userMap = userProfileRepository.findAllByIdIn(assessorIdList)
                    .stream()
                    .collect(Collectors.toMap(UserProfile::getId, user -> user));

        }

        List<UserSeason> userSeasonList = userSeasonRepository.findAllByIdList(userSeasonIdList);
        //获取举报对应的评分任务id
        List<UserReportDetail> userReportDetailList = userReportDetailRepository.findAllByAssessTaskIdIn(assessTaskIdList);
        Map<Long, UserReportDetail> userReportDetailMap = userReportDetailList.stream().collect(Collectors.toMap(UserReportDetail::getAssessTaskId, detail -> detail));

        Map<Long, Long> userSeasonMap = new HashMap<>();
        if (null != userSeasonList && userSeasonList.size() > 0) {
            userSeasonList.forEach(us -> userSeasonMap.put(us.getId(), us.getUserId()));
        }
        Map<Long, UserProfile> finalUserMap = userMap;
        List<AssessScoreDTO> result = assessTasks
                .stream()
                .map(assessTask -> {
                    AssessScoreDTO assessScoreDTO = new AssessScoreDTO();
                    assessScoreDTO.setId(assessTask.getId());
                    assessScoreDTO.setAssessorId(assessTask.getAssessorId());
                    assessScoreDTO.setScore(assessTask.getScore());
                    assessScoreDTO.setStatus(assessTask.getStatus());
                    Long userId = userSeasonMap.get(assessTask.getUserSeasonId());
                    if (null != userId) {
                        assessScoreDTO.setReportAssessTask(currentUserId.equals(userId));
                    }
                    UserReportDetail detail = userReportDetailMap.get(assessTask.getId());
                    if (null != detail) {
                        assessScoreDTO.setReportStatus(detail.getStatus());
                        assessScoreDTO.setReportStatusValue(String.format("举报(%s)", UserReportDetailStatusEnum.getValueByCode(detail.getStatus())));
                    }
                    if (null != finalUserMap && finalUserMap.size() > 0) {
                        assessScoreDTO.setAssessorRealName(finalUserMap.get(assessScoreDTO.getAssessorId()).getRealName());
                        assessScoreDTO.setAssessorProfilePhoto(finalUserMap.get(assessScoreDTO.getAssessorId()).getProfilePhoto());
                    }
                    return assessScoreDTO;
                }).collect(Collectors.toList());


        return result;
    }


    /**
     * 获取对此评价的项目
     *
     * @param taskId 评价任务id
     * @return
     */
    public List<UserScoreItemDTO> getDetailForAssessScoreItems(Long taskId) {
        List<AssessTaskItem> assessTaskItemList = assessTaskItemRepository.findAllByTaskId(taskId);
        Map<Long, ScoreOption> scoreOptionMap = scoreOptionRepository.findAllByType(null)
                .stream()
                .collect(Collectors.toMap(ScoreOption::getId, scoreOption -> scoreOption));

        return assessTaskItemList
                .stream()
                .map(assessTaskItem -> {
                    UserScoreItemDTO userScoreItemDTO = new UserScoreItemDTO();
                    userScoreItemDTO.setOptionId(assessTaskItem.getOptionId());
                    userScoreItemDTO.setScore(assessTaskItem.getScore());

                    ScoreOption scoreOption = scoreOptionMap.get(assessTaskItem.getOptionId());
                    userScoreItemDTO.setOptionName(scoreOption.getName());
                    userScoreItemDTO.setOptionTypeCode(scoreOption.getType());
                    return userScoreItemDTO;
                })
                .collect(Collectors.toList());
    }


    /**
     * 查询用户考核详情评分明细
     *
     * @param userSeasonId
     * @return 雷达图集合
     */
    public CommonResult queryUserSeasonItemDetail(Long userSeasonId) {
        List<UserSeasonRadarVM> radarVMList = Lists.newArrayList();
        //获取评分项
        List<ScoreOption> scoreOptions = scoreOptionRepository.findAllByType(null);
        Map<Long, ScoreOption> scoreOptionMap = scoreOptions.stream().collect(Collectors.toMap(ScoreOption::getId, so -> so));

        //获取用户考核综合分
        List<UserSeasonItem> complexList = userSeasonItemRepository.findAllByUserSeasonIdIn(Lists.newArrayList(userSeasonId));
        Map<Long, BigDecimal> complexMap = Maps.newHashMap();
        complexList.forEach(item -> {
            complexMap.put(item.getOptionId(), item.getScore());
        });
        radarVMList.add(convertUserSeasonRadarVM(scoreOptionMap, complexMap, 0, "综合"));
        //获取其他评价
        List<AssessTask> assessTaskList = assessTaskRepository.findAllByUserSeasonId(userSeasonId);
        if (null != assessTaskList && assessTaskList.size() > 0) {
            List<Long> taskIdList = assessTaskList.stream().map(AssessTask::getId).collect(Collectors.toList());
            //获取所有评价对应的举报
            List<UserReportDetail> userReportDetailList = userReportDetailRepository.findAllByAssessTaskIdIn(taskIdList);
            Map<Long, UserReportDetail> userReportDetailMap = Maps.newHashMap();
            UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(userSeasonId);
            //是否可以举报（当前季度考核为当前用户的考核是）
            //boolean canReport = userSeason.getUserId().equals(SecurityUtils.getCurrentUserId());
            //所有人都可以举报任何评价分，不做限制
            boolean canReport = true;
            if (null != userReportDetailList && userReportDetailList.size() > 0) {
                userReportDetailMap = userReportDetailList.stream().collect(Collectors.toMap(UserReportDetail::getAssessTaskId, detail -> detail));
            }
            List<AssessTaskItem> allTaskItemList = assessTaskItemRepository.findAllByTaskIdList(taskIdList);
            radarVMList.addAll(convertUserVM(scoreOptionMap, allTaskItemList, userReportDetailMap, canReport));
        }
        return CommonResult.success(radarVMList);
    }

    private UserSeasonRadarVM convertUserSeasonRadarVM(Map<Long, ScoreOption> scoreOptionMap, Map<Long, BigDecimal> complexMap, int sort, String name) {
        RadarVM radarVM = convertRadarVM(scoreOptionMap, complexMap, sort, name);
        UserSeasonRadarVM userSeasonRadarVM = new UserSeasonRadarVM();
        BeanUtils.copyProperties(radarVM, userSeasonRadarVM);
        return userSeasonRadarVM;
    }

    /**
     * 生成综合雷达图对象
     *
     * @param scoreOptionMap
     * @param complexMap
     * @return
     */
    private RadarVM convertRadarVM(Map<Long, ScoreOption> scoreOptionMap, Map<Long, BigDecimal> complexMap, int sort, String name) {
        //ACTUALIZE 价值与能力
        List<BigDecimal> actualizeList = Lists.newArrayList();
        List<String> actualizeOptionItemList = Lists.newArrayList();
        //ATTITUDE 态度与为人
        List<BigDecimal> attitudeList = Lists.newArrayList();
        List<String> attitudeOptionItemList = Lists.newArrayList();
        for (Map.Entry<Long, ScoreOption> entry : scoreOptionMap.entrySet()) {
            ScoreOption scoreOption = entry.getValue();
            Byte type = scoreOption.getType();
            BigDecimal score = complexMap.get(entry.getKey());
            score = null == score ? BigDecimal.ZERO : score;
            if (OptionType.ACTUALIZE.getCode() == type) {
                actualizeList.add(score);
                actualizeOptionItemList.add(scoreOption.getName());
            } else if (OptionType.ATTITUDE.getCode() == type) {
                attitudeList.add(score);
                attitudeOptionItemList.add(scoreOption.getName());
            }
        }
        RadarVM vm = new RadarVM();
        vm.setActualize(actualizeList);
        vm.setActualizeOptionItem(actualizeOptionItemList);
        vm.setAttitude(attitudeList);
        vm.setAttitudeOptionItem(attitudeOptionItemList);
        vm.setSort(sort);
        vm.setName(name);
        return vm;
    }

    /**
     * 生成用户评分雷达图
     *
     * @param scoreOptionMap      评分项
     * @param allTaskItemList     评分项打分明细
     * @param userReportDetailMap 用户举报明细
     * @param canReport           是否可以举报
     * @return
     */
    private List<UserSeasonRadarVM> convertUserVM(Map<Long, ScoreOption> scoreOptionMap, List<AssessTaskItem> allTaskItemList, Map<Long, UserReportDetail> userReportDetailMap, boolean canReport) {
        Map<Long, Map<Long, BigDecimal>> taskMap = Maps.newConcurrentMap();
        allTaskItemList.forEach(taskItem -> {
            Long taskId = taskItem.getTaskId();
            Map<Long, BigDecimal> itemMap = OkrMapUtil.newHashMapIfNull(taskMap.get(taskId));
            itemMap.put(taskItem.getOptionId(), taskItem.getScore());
            taskMap.put(taskId, itemMap);
        });
        List<UserSeasonRadarVM> list = Lists.newArrayList();

        for (Map.Entry<Long, Map<Long, BigDecimal>> entry : taskMap.entrySet()) {
            int sort = list.size() + 1;
            UserSeasonRadarVM userSeasonRadarVM = convertUserSeasonRadarVM(scoreOptionMap, entry.getValue(), sort, "评价人-" + sort);
            //设置举报标识
            userSeasonRadarVM.setCanReport(canReport);
            UserReportDetail detail = userReportDetailMap.get(entry.getKey());
            userSeasonRadarVM.setAssessTaskId(entry.getKey());
            if (null != detail) {
                userSeasonRadarVM.setUserReportStatus(detail.getStatus());
                userSeasonRadarVM.setUserReportStatusText(UserReportDetailStatusEnum.getValueByCode(detail.getStatus()));
            }

            list.add(userSeasonRadarVM);
        }
        return list;
    }


    /**
     * 获取最新季度评分
     *
     * @return
     */
    public List<UserScoreItemDTO> getLastSeasonScoreItem() {
        Season query = new Season();
        query.setStatus(SeasonStatus.FINISH.getCode());
        query.setMakeSeasonScore(true);
        //跳过最新的考核
        List<Season> seasonList = seasonRepository.findAllBySeason(query, 0, 1);
        if (null == seasonList || seasonList.size() == 0) {
            return new ArrayList<>();
        }

        Season season = seasonList.get(0);
        UserSeason userSeason = userSeasonRepository.findOneBySeasonIdAndUserId(season.getId(), SecurityUtils.getCurrentUserId());
        if (userSeason == null) {
            return new ArrayList<>();
        }
        Map<Long, UserSeasonItem> userScoreItemMap = userSeasonItemRepository
                .findAllByUserSeasonIdIn(Lists.newArrayList(userSeason.getId()))
                .stream()
                .collect(Collectors.toMap(UserSeasonItem::getOptionId, userScoreItem -> userScoreItem));

        if (userScoreItemMap.size() == 0 || userScoreItemMap.size() == 1) {
            return new ArrayList<>();
        }

        List<ScoreOption> scoreOptions = scoreOptionRepository.findAllByType(null);
        return getUserScoreItemDTOS(userScoreItemMap, scoreOptions);
    }

    /**
     * 获取最新季度评分
     *
     * @param userScoreItemMap 得分项map
     * @param scoreOptions     得分项类型枚举
     * @return
     */
    private List<UserScoreItemDTO> getUserScoreItemDTOS
    (Map<Long, UserSeasonItem> userScoreItemMap, List<ScoreOption> scoreOptions) {
        return scoreOptions
                .stream()
                .map(scoreOption -> {
                    UserScoreItemDTO userScoreDTO = new UserScoreItemDTO();
                    userScoreDTO.setOptionId(scoreOption.getId());
                    userScoreDTO.setOptionName(scoreOption.getName());
                    userScoreDTO.setOptionTypeCode(scoreOption.getType());

                    UserSeasonItem userScoreItem = userScoreItemMap.get(scoreOption.getId());
                    if (userScoreItem != null) {
                        userScoreDTO.setScore(userScoreItem.getScore());
                    }
                    return userScoreDTO;
                })
                .collect(Collectors.toList());
    }


    /**
     * 获取季度排行榜
     *
     * @param seasonId      季度考核id
     * @param scoreOptionId 得分项
     * @param assessResult  评价结果
     * @param pageable      分页
     * @return
     */
    public Page<RankingDTO> getRanking(Long seasonId, Long scoreOptionId, Integer assessResult, Pageable pageable) {
        //如果 当前季度考核未结束 & 非管理员角色，不能查看排名
        Season season = seasonRepository.selectByPrimaryKey(seasonId);
        if (season != null && !season.isMakeSeasonScore()) {
            if (SecurityUtils.getUserOperatorPerm().getCanSeeRankingList().equals(YesOrNoEnum.NO.getCode())) {
                return new PageImpl<RankingDTO>(Lists.newArrayList());
            }
        }

        long count = userSeasonRepository.countRankingList(seasonId, scoreOptionId, assessResult);
        List<RankingDTO> rankingDTOs = new ArrayList<>();
        if (count > 0) {
            int start = pageable.getPageNumber() * pageable.getPageSize();
            rankingDTOs = userSeasonRepository.queryRankingList(seasonId, scoreOptionId, assessResult, start, pageable.getPageSize());
            AwsPictureProcessUtil.assembleRankingDTO(rankingDTOs);
            //设置用户排名
            for (int index = 0; index < rankingDTOs.size(); index++) {
                RankingDTO dto = rankingDTOs.get(index);
                dto.setRanking(start + index + 1);
            }
        }
        return new PageImpl<>(rankingDTOs, pageable, count);
    }


    /**
     * 获取公司okr概览列表
     *
     * @param seasonId
     * @param deptId
     * @param pageable
     * @return
     */
    public Page<RankingDTO> getCompanyOkrOverview(Long seasonId, Long deptId, Pageable pageable) {

        Department department = departmentRepository.selectByPrimaryKey(deptId);
        //获取所有机构子集部门
        List<Long> depIds = departmentService.getDepartmentIdAndChildrens(Lists.newArrayList(department.getId()));
        long count = userSeasonRepository.countCompanyOkrOverview(seasonId, depIds);

        List<RankingDTO> rankingDTOs = new ArrayList<>();
        if (count > 0) {
            //判读排名是否为空，基于不为空的排名升序
            rankingDTOs = userSeasonRepository.queryCompanyOkrOverview(seasonId, depIds, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            List<Long> userSeasonIdList = rankingDTOs.stream().map(dto -> dto.getId()).collect(Collectors.toList());
            Map<Long, XYAxisVM> xyAxisVMMap = queryUserSeasonItemViewMap(userSeasonIdList);
            rankingDTOs.forEach(dto -> {
                dto.setXyAxisVM(xyAxisVMMap.get(dto.getId()));
            });
        }
        Page<RankingDTO> result = new PageImpl(rankingDTOs, pageable, count);
        return result;
    }


    /**
     * 获取未完成OKR的员工
     *
     * @param seasonId
     * @param pageable
     * @return
     */
    public Page<UserInfoLiteDTO> getUnFinishFilledOkrList(Long seasonId, Pageable pageable) {
        List<UserInfoLiteDTO> userInfoLiteDTOs = new ArrayList<>();
        long count = userSeasonRepository.countUnFinishFilledOkrList(seasonId);
        if (count > 0) {
            userInfoLiteDTOs = userSeasonRepository.queryUnFinishFilledOkrList(seasonId, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            AwsPictureProcessUtil.assembleUserInfoLiteDTO(userInfoLiteDTOs);
        }
        Page<UserInfoLiteDTO> result = new PageImpl(userInfoLiteDTOs, pageable, count);
        return result;
    }


    /**
     * 统计 考核用户相关统计
     *
     * @param seasonId
     * @return
     */
    public CommonResult<UserSeasonCountDTO> querySeasonUserInfoCount(Long seasonId) {
        if (null == seasonId || seasonId <= 0) {
            return CommonResult.error("入参异常");
        }
        UserSeasonCountDTO dto = new UserSeasonCountDTO();
        //获取总参与考核的人数
        long countOKRUser = userSeasonRepository.countOKRUser(seasonId);
        dto.setOkrUserCount(countOKRUser);
        //统计为完成OKR的员工的数量
        long unFinishFilledOkrCount = userSeasonRepository.countUnFinishFilledOkrList(seasonId);
        dto.setUnFinishFilledCount(unFinishFilledOkrCount);
        long countAllAssess = userSeasonRepository.countAllAssess(seasonId);
        dto.setOkrAssessCount(countAllAssess);
        //为完成的考评人数
        long unFinishAssessCount = userSeasonRepository.countUnFinishAssessList(seasonId, Lists.newArrayList(AssessTaskStatus.UNDERWAY.getCode(), AssessTaskStatus.NOT_ASSESS.getCode()));
        dto.setUnFinishAssessCount(unFinishAssessCount);

        return CommonResult.success(dto);
    }

    /**
     * 获取未填写OKR目标的人员
     *
     * @param seasonId
     * @return
     */
    public List<UserInfoDTO> getUnFinishFilledOkrList(Long seasonId) {
        return userSeasonRepository.getUnFinishFilledOkrList(seasonId);
    }

    /**
     * 获取未完成选人的员工
     *
     * @param seasonId 季度考核id
     * @param pageable 分页
     * @return
     */
    public Page<UserInfoLiteDTO> getUnSelectedAssessorList(Long seasonId, Pageable pageable) {
        long count = userSeasonRepository.countUnSelectedAssessorList(seasonId);
        List<UserInfoLiteDTO> userInfoLiteDTOs = new ArrayList<>();
        if (count > 0) {
            userInfoLiteDTOs = userSeasonRepository.queryUnSelectedAssessorList(seasonId, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        }
        Page<UserInfoLiteDTO> result = new PageImpl(userInfoLiteDTOs, pageable, count);
        return result;
    }

    /**
     * 获取未完成评价任务的员工
     *
     * @param seasonId
     * @param pageable
     * @return
     */
    public Page<UserInfoLiteDTO> getUnFinishAssessList(Long seasonId, Pageable pageable) {
        long count = userSeasonRepository.countUnFinishAssessList(seasonId, Lists.newArrayList(AssessTaskStatus.UNDERWAY.getCode(), AssessTaskStatus.NOT_ASSESS.getCode()));
        List<UserInfoLiteDTO> userInfoLiteDTOs = new ArrayList<>();
        if (count > 0) {
            userInfoLiteDTOs = userSeasonRepository.queryUnFinishAssessList(seasonId, Lists.newArrayList(AssessTaskStatus.UNDERWAY.getCode(), AssessTaskStatus.NOT_ASSESS.getCode()), pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            AwsPictureProcessUtil.assembleUserInfoLiteDTO(userInfoLiteDTOs);
        }
        Page<UserInfoLiteDTO> result = new PageImpl(userInfoLiteDTOs, pageable, count);
        return result;
    }


    /**
     * 用户考核比对结果
     *
     * @param userSeasonIdList 用户考核ID集合
     * @return
     */
    public CommonResult getUserSeasonCompareDetail(List<Long> userSeasonIdList) {
        if (null == userSeasonIdList || userSeasonIdList.isEmpty()) {
            log.error("用户考核比对结果----入参为空");
            return CommonResult.error("入参为空");
        }
        List<UserSeason> userSeasonList = userSeasonRepository.findAllByIdList(userSeasonIdList);
        if (null == userSeasonList || userSeasonList.isEmpty()) {
            log.error("用户考核比对结果----用户考核ID：{}", JSON.toJSONString(userSeasonIdList));
            return CommonResult.error("用户考核信息不存在");
        }
        //key:用户考核ID，入参Id value:入参对应的明细
        Map<Long, UserSeasonCompareDTO> compareDTOMap = Maps.newHashMap();
        //基于用户考核id生成对应的map
        userSeasonIdList.forEach(id -> {
            compareDTOMap.put(id, new UserSeasonCompareDTO());
        });
        List<Long> userIdList = Lists.newArrayList();
        //key:用户id value:用户考核ID
        Map<Long, Long> userSeasonMap = Maps.newHashMap();
        userSeasonList.forEach(userSeason -> {
            Long userId = userSeason.getUserId();
            userIdList.add(userId);
            userSeasonMap.put(userId, userSeason.getId());
        });

        //封装用户信息DTO
        buildUserInfoDTO(compareDTOMap, userSeasonMap, userIdList);
        //封装用户考评项得分
        buildUserScoreItem(userSeasonIdList, compareDTOMap);
        //封装评分详情
        buildAssessTask(userSeasonIdList, compareDTOMap);
        //封装OKR季度目标
        buildOkrContent(userSeasonIdList, compareDTOMap);
        return CommonResult.success(compareDTOMap);
    }

    /**
     * 封装OKR季度目标
     *
     * @param userSeasonIdList 用户考核ID集合
     * @param compareDTOMap    封装的比较对象map
     */
    private void buildOkrContent(List<Long> userSeasonIdList, Map<Long, UserSeasonCompareDTO> compareDTOMap) {
        List<OkrContent> orkContentList = okrContentRepository.findAllByUserSeasonIdList(userSeasonIdList);
        orkContentList.forEach(okrContent -> {
            UserSeasonCompareDTO dto = compareDTOMap.get(okrContent.getUserSeasonId());
            List<OkrContentDTO> okrDtoList = OKRListUtil.newArrayListIfNull(dto.getOkrContentDTOList());
            okrDtoList.add(new OkrContentDTO(okrContent));
            dto.setOkrContentDTOList(okrDtoList);
        });
    }

    /**
     * 封装评分详情
     *
     * @param userSeasonIdList 用户考核ID集合
     * @param compareDTOMap    封装的比较对象map
     */
    private void buildAssessTask(List<Long> userSeasonIdList, Map<Long, UserSeasonCompareDTO> compareDTOMap) {
        List<AssessTask> assessTaskList = assessTaskRepository.findAllByUserSeasonIdList(userSeasonIdList);
        assessTaskList.forEach(assessTask -> {
            //获取有效状态的考核
            if (AssessTaskStatus.codeIsValid(assessTask.getStatus())) {
                UserSeasonCompareDTO dto = compareDTOMap.get(assessTask.getUserSeasonId());
                List<AssessScoreDTO> scoreDTOList = OKRListUtil.newArrayListIfNull((dto.getAssessScoreDTOList()));
                AssessScoreDTO asDto = new AssessScoreDTO();
                asDto.setScore(assessTask.getScore());
                asDto.setId(assessTask.getId());
                scoreDTOList.add(asDto);
                dto.setAssessScoreDTOList(scoreDTOList);
            }
        });
    }

    /**
     * 封装用户考评项得分
     *
     * @param userSeasonIdList 用户考核ID集合
     * @param compareDTOMap    封装的比较对象map
     */
    private void buildUserScoreItem(List<Long> userSeasonIdList, Map<Long, UserSeasonCompareDTO> compareDTOMap) {
        Map<Long, String> optionsMap = scoreOptionService.getAllOptionsMap();
        List<UserSeasonItem> userSeasonItemList = userSeasonItemRepository.findAllByUserSeasonIdIn(userSeasonIdList);
        userSeasonItemList.forEach(userSeasonItem -> {
            UserSeasonCompareDTO dto = compareDTOMap.get(userSeasonItem.getUserSeasonId());
            Long optionId = userSeasonItem.getOptionId();
            if (optionId > 0) {
                List<UserScoreItemDTO> itemDTOList = OKRListUtil.newArrayListIfNull(dto.getUserScoreItemDTOS());
                UserScoreItemDTO itemDTO = new UserScoreItemDTO();
                itemDTO.setOptionId(optionId);
                String optionName = optionsMap.get(optionId);
                itemDTO.setOptionName(optionName);
                itemDTO.setScore(userSeasonItem.getScore());
                itemDTO.setRanking(userSeasonItem.getRanking());
                itemDTOList.add(itemDTO);
                //排序
                sortUserScoreItemDTOListByOptionId(itemDTOList);
                dto.setUserScoreItemDTOS(itemDTOList);
            } else {
                dto.setScore(userSeasonItem.getScore());
                dto.setRanking(userSeasonItem.getRanking());
            }
        });
    }


    /**
     * 根据评分项排序
     *
     * @param list
     */
    private void sortUserScoreItemDTOListByOptionId(List<UserScoreItemDTO> list) {
        list.sort((o1, o2) -> (int) (o1.getOptionId() - o2.getOptionId()));
    }

    /**
     * 封装用户信息dto
     *
     * @param compareDTOMap 封装的比较对象map
     * @param userIdList    考核用户ID集合
     */
    private void buildUserInfoDTO
    (Map<Long, UserSeasonCompareDTO> compareDTOMap, Map<Long, Long> userSeasonMap, List<Long> userIdList) {
        List<UserInfoDTO> userInfoDTOList = userProfileRepository.findUserDetailByIdIn(userIdList);
        userInfoDTOList.forEach(userInfoDTO -> {
            Long userId = userInfoDTO.getId();
            UserSeasonCompareDTO dto = compareDTOMap.get(userSeasonMap.get(userId));
            dto.setUserInfoDTO(userInfoDTO);
        });
    }

    /**
     * 查询用户考核视图信息
     *
     * @param isFirst 是否第一条
     * @return
     */
    public CommonResult queryUserSeasonViews(boolean isFirst) {

        Long currentUserId = SecurityUtils.getCurrentUserId();
        //获取当前用户职级
        UserProfile userProfile = userProfileRepository.selectByPrimaryKey(currentUserId);
        String userRank = userProfile.getRank();
        //需要被评价的数量
        long adjustedNeedCount = 0L;
        if (StringUtils.isNotBlank(userRank)) {
            adjustedNeedCount = adjusterService.getAdjusterCountFromRedisByRankContains(userRank);
        }

        List<UserSeasonDTO> dtoList = userSeasonRepository.queryViewDataByUserIdAndStatus(AssessStatus.UNDERWAY.getCode(), currentUserId);
        for (UserSeasonDTO dto : dtoList) {
            ProgressStatusEnum processStatus = SeasonDateUtil.getProcessStatus(dto);
            dto.setProgressStatus(processStatus.getCode());
            dto.setProgressStatusText(processStatus.getName());
            //判断考评任务是否开始
            dto.setAssessTaskIsBegin(ProgressStatusEnum.SECOND_IN.getCode().equals(processStatus.getCode()));
            dto.setUserRank(userRank);
            dto.setAdjustedNeedCount(adjustedNeedCount);

            //设置已选择考评对应数量
            List<UserInfoDTO> selectedUserList = adjusterService.getSelectedUsers(dto.getId(), UserStatus.NORMAL.getCode());
            if (selectedUserList != null && selectedUserList.size() != 0) {
                dto.setAdjustCount((long) selectedUserList.size());
            }
        }
        //判断是否是展示一条数据
        if (isFirst && dtoList.size() > 0) {
            return CommonResult.success(dtoList.subList(0, 1));
        }
        return CommonResult.success(dtoList);
    }


    /**
     * 获取当前用户的的得分排名
     *
     * @return
     */
    public XYAxisVM queryUserSeasonRankView(Long userId) {
        List<XYAxisVM> vmList = userSeasonRepository.queryUserAllSeasonRank(userId);
        XYAxisVM vm = XYAxisVMUtil.convertList2Vm(vmList);
        Integer diff = XYAxisVMUtil.getYDiffInteger(vmList);
        vm.setyDiff(String.valueOf(diff));
        vm.setyDiffPositive(diff >= 0);
        vm.setShow(vmList.size() >= XYAxisVMUtil.sizeShow);
        return vm;
    }


    /**
     * 获取当前用户的的得分排名
     *
     * @return
     */
    public XYAxisVM queryUserSeasonScoreView(Long userId) {
        List<XYAxisVM> vmList = userSeasonRepository.queryUserAllSeasonScore(userId);
        XYAxisVM vm = XYAxisVMUtil.convertList2Vm(vmList);
        BigDecimal diff = XYAxisVMUtil.getYDiffBigDecimal(vmList);
        vm.setyDiff(String.valueOf(diff));
        vm.setyDiffPositive(diff.compareTo(BigDecimal.ZERO) >= 0);
        vm.setShow(vmList.size() >= XYAxisVMUtil.sizeShow);
        return vm;
    }


    /**
     * 查询当前用户考核信息
     *
     * @return
     */
    public CommonResult queryCurrentUserSeasonInfo() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserSeason userSeason = userSeasonRepository.queryLastInfoByUserId(currentUserId);
        if (userSeason == null) {
            return CommonResult.error("用户未参与该考核");
        }
        UserSeasonDTO dto = new UserSeasonDTO();
        UserSeasonItem item = userSeasonItemRepository.findOneByUserSeasonIdAndOptionId(userSeason.getId(), 0L);
        if (null != item) {
            dto.setScore(item.getScore());
            dto.setRanking(item.getRanking());
        }
        //设置得分的xy视图
        dto.setRankXYAxis(queryUserSeasonRankView(currentUserId));
        dto.setScoreXYAxis(queryUserSeasonScoreView(currentUserId));
        UserReport userReport = userReportRepository.findByUserId(currentUserId);
        if (null != userReport) {
            dto.setReportedCount(userReport.getReportedCount());
            //dto.setAssessCount(userReport.getAssessCount());
            //设置评价数量 todo 原处理有问题，只有真正评分的才能计算在内
            long assessCount = assessTaskRepository.countValidAssessTask(currentUserId, AssessTaskStatus.FINISHED.getCode());
            dto.setAssessCount((int) assessCount);
        }
        return CommonResult.success(dto);
    }

    /**
     * 根据用户考核id查询用户考核评分项维度
     *
     * @param userSeasonIdList
     * @return
     */
    public Map<Long, XYAxisVM> queryUserSeasonItemViewMap(List<Long> userSeasonIdList) {
        Map<Long, XYAxisVM> xyMap = Maps.newHashMap();
        //获取所有的用户考核记录
        List<UserSeasonItem> itemList = userSeasonItemRepository.findAllByUserSeasonIdIn(userSeasonIdList);
        Map<Long, Map<Long, BigDecimal>> itemMap = Maps.newHashMap();
        if (null != itemList && itemList.size() > 0) {
            for (UserSeasonItem item : itemList) {
                Long userSeasonId = item.getUserSeasonId();
                Map<Long, BigDecimal> scoreMap = OkrMapUtil.newHashMapIfNull(itemMap.get(userSeasonId));
                scoreMap.put(item.getOptionId(), item.getScore());
                itemMap.put(userSeasonId, scoreMap);
            }
        }
        List<ScoreOption> optionList = scoreOptionRepository.findAllByType(null);
        Map<Long, String> optionMap = optionList.stream().collect(Collectors.toMap(ScoreOption::getId, ScoreOption::getName));
        //确保所有的查询都会有对应的数据
        AssessTaskService.constructXYAxisMap(userSeasonIdList, xyMap, itemMap, optionMap);
        return xyMap;
    }


    /**
     * 查询最近用户考核排名
     *
     * @return
     */
    public CommonResult queryLastUserRankingInfo() {
        Season season = getLastOverSeason();

        if (season == null) {
            return CommonResult.error("不存在已经结束的季度考核信息");
        }

        //获取最近的一次季度考核
        List<UserSeasonDTO> list = userSeasonRepository.queryUserRankingInfoBySeasonIdAndNum(season.getId(), CommonConstants.NUM_5);
        return CommonResult.success(list);
    }


    /**
     * 获取已经结束的最近一次季度考核的userSeason数据
     *
     * @param userId
     * @return
     */
    public CommonResult queryLastOverUserSeason(Long userId) {
        Season season = getLastOverSeason();
        if (season == null) {
            return CommonResult.error("不存在已经结束的季度考核信息");
        }
        UserSeason userSeason = userSeasonRepository.findOneBySeasonIdAndUserId(season.getId(), userId);
        return CommonResult.success(userSeason);
    }


    /**
     * 获取已经结束的最近一次的考核
     *
     * @return
     */
    private Season getLastOverSeason() {
        Season query = new Season();
        query.setStatus(SeasonStatus.FINISH.getCode());
        query.setMakeSeasonScore(true);
        //跳过最新的考核
        List<Season> seasonList = seasonRepository.findAllBySeason(query, 0, 1);
        if (null == seasonList || seasonList.size() == 0) {
            return null;
        }
        return seasonList.get(0);
    }
}
