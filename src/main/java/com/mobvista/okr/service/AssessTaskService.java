package com.mobvista.okr.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.domain.*;
import com.mobvista.okr.dto.*;
import com.mobvista.okr.enums.AssessTaskStatus;
import com.mobvista.okr.enums.OptionType;
import com.mobvista.okr.enums.ProgressStatusEnum;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.repository.*;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.util.*;
import com.mobvista.okr.vm.AssessTaskViewVM;
import com.mobvista.okr.vm.MakeScoreVM;
import com.mobvista.okr.vm.XYAxisVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing adjuster.
 */
@Service
public class AssessTaskService {


    @Resource
    private AssessTaskRepository assessTaskRepository;

    @Resource
    private DepartmentRepository departmentRepository;
    @Resource
    private UserSeasonRepository userSeasonRepository;
    @Resource
    private ScoreOptionRepository scoreOptionRepository;
    @Resource
    private AssessTaskItemRepository assessTaskItemRepository;
    @Resource
    private SeasonRepository seasonRepository;
    @Resource
    private MakeScoreAndRankService makeScoreAndRankService;
    @Resource
    private UserReportService userReportService;
    @Resource
    private UserRankService userRankService;
    @Resource
    private DepartmentService departmentService;

//    /**
//     * 我的待评价列表
//     *
//     * @param pageable
//     * @param assessStatus 考评状态
//     */
//    public Page<AssessTaskDTO> myAssessTaskList(Pageable pageable, Byte assessStatus) {
//        List<AssessTaskDTO> assessTaskDTOs = new ArrayList<>();
//        long count = assessTaskRepository.countPageByAssessorIdAndStatus(SecurityUtils.getCurrentUserId(), assessStatus);
//        if (count > 0) {
//            assessTaskDTOs = assessTaskRepository.queryListByAssessorIdAndStatus(SecurityUtils.getCurrentUserId(), assessStatus, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
//            List<Long> taskIdList = assessTaskDTOs.stream().map(AssessTaskDTO::getId).collect(Collectors.toList());
//            Map<Long, XYAxisVM> xyAxisVMMap = queryAssessTaskItemView(taskIdList);
//            //设置考核进度状态
//            assessTaskDTOs.forEach(dto -> {
//                ProgressStatusEnum processStatus = SeasonDateUtil.getProcessStatus(dto);
//                dto.setProgressStatus(processStatus.getCode());
//                dto.setProgressStatusText(processStatus.getName());
//                dto.setStatusText(AssessTaskStatus.getValueByCode(dto.getStatus()));
//                dto.setXyAxisVM(xyAxisVMMap.get(dto.getId()));
//            });
//        }
//        Page<AssessTaskDTO> result = new PageImpl(assessTaskDTOs, pageable, count);
//        return result;
//    }


    /**
     * 季度考核维度统计评价任务
     *
     * @param pageable
     * @param assessStatus
     * @return
     */
    public Page<AssessTaskSeasonDTO> myAssessTaskSeasonList(Pageable pageable, Byte assessStatus) {
        List<AssessTaskSeasonDTO> list = new ArrayList<>();
        long count = assessTaskRepository.countSeasonPageByAssessorIdAndStatus(SecurityUtils.getCurrentUserId(), assessStatus);
        if (count > 0) {
            list = assessTaskRepository.querySeasonPageByAssessorIdAndStatus(SecurityUtils.getCurrentUserId(), assessStatus, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            List<Long> seasonIdList = list.stream().map(AssessTaskSeasonDTO::getSeasonId).collect(Collectors.toList());
            List<AssessTaskDTO> taskDtoList = assessTaskRepository.queryListByAssessorIdAndStatus(SecurityUtils.getCurrentUserId(), assessStatus, seasonIdList);
            List<Long> taskIdList = Lists.newArrayList();
            Map<Long, List<AssessTaskDTO>> seasonTaskMap = Maps.newHashMap();
            taskDtoList.forEach(taskDTO -> {
                taskIdList.add(taskDTO.getId());
                Long seasonId = taskDTO.getSeasonId();
                List<AssessTaskDTO> taskList = OKRListUtil.newArrayListIfNull(seasonTaskMap.get(seasonId));
                taskList.add(taskDTO);
                seasonTaskMap.put(seasonId, taskList);

            });
            Map<Long, XYAxisVM> xyAxisVMMap = queryAssessTaskItemView(taskIdList);
            //设置考核进度状态
            list.forEach(dto -> {
                List<AssessTaskDTO> taskList = seasonTaskMap.get(dto.getSeasonId());
                int underwayNum = 0;
                for (AssessTaskDTO taskDTO : taskList) {
                    ProgressStatusEnum processStatus = SeasonDateUtil.getProcessStatus(taskDTO);
                    taskDTO.setProgressStatus(processStatus.getCode());
                    taskDTO.setProgressStatusText(processStatus.getName());
                    Byte status = taskDTO.getStatus();
                    if (AssessTaskStatus.UNDERWAY.getCode() == status) {
                        underwayNum++;
                    }
                    taskDTO.setStatusText(AssessTaskStatus.getValueByCode(status));
                    taskDTO.setXyAxisVM(xyAxisVMMap.get(taskDTO.getId()));
                }
                taskList.sort(Comparator.comparingInt(AssessTaskDTO::getStatus));
                //封装亚马逊图片
                AwsPictureProcessUtil.assembleAssessTaskDTO(taskList);

                dto.setAssessTaskDTOList(taskList);
                dto.setUnderwayNum(underwayNum);
            });
        }
        Page<AssessTaskSeasonDTO> result = new PageImpl(list, pageable, count);
        return result;
    }


    /**
     * 根据用户考核id查询用户考核评分项维度
     *
     * @param taskIdList
     * @return
     */
    public Map<Long, XYAxisVM> queryAssessTaskItemView(List<Long> taskIdList) {
        Map<Long, XYAxisVM> xyMap = Maps.newHashMap();
        //获取所有的用户考核记录
        List<AssessTaskItem> itemList = assessTaskItemRepository.findAllByTaskIdList(taskIdList);
        Map<Long, Map<Long, BigDecimal>> itemMap = Maps.newHashMap();
        if (null != itemList && itemList.size() > 0) {
            for (AssessTaskItem item : itemList) {
                Long taskId = item.getTaskId();
                Map<Long, BigDecimal> scoreMap = OkrMapUtil.newHashMapIfNull(itemMap.get(taskId));
                scoreMap.put(item.getOptionId(), item.getScore());
                itemMap.put(taskId, scoreMap);
            }
        }
        List<ScoreOption> optionList = scoreOptionRepository.findAllByType(null);
        Map<Long, String> optionMap = optionList.stream().collect(Collectors.toMap(ScoreOption::getId, ScoreOption::getName));
        //确保所有的查询都会有对应的数据
        constructXYAxisMap(taskIdList, xyMap, itemMap, optionMap);
        return xyMap;
    }

    /**
     * 构造 折线图map
     *
     * @param idList
     * @param xyMap
     * @param itemMap
     * @param optionMap
     */
    static void constructXYAxisMap(List<Long> idList, Map<Long, XYAxisVM> xyMap, Map<Long, Map<Long, BigDecimal>> itemMap, Map<Long, String> optionMap) {
        for (Long taskId : idList) {
            Map<Long, BigDecimal> scoreMap = itemMap.get(taskId);
            List<XYAxisVM> vmsList = Lists.newArrayList();
            XYAxisVM xyAxisVM;
            //根据相同的考核类型排序
            for (Map.Entry<Long, String> entry : optionMap.entrySet()) {
                xyAxisVM = new XYAxisVM();
                xyAxisVM.setX(entry.getValue());
                xyAxisVM.setxSortFlag(entry.getKey());
                BigDecimal score = null;
                if (scoreMap != null) {
                    score = scoreMap.get(entry.getKey());
                }
                score = score != null ? score : BigDecimal.ZERO;
                xyAxisVM.setY(String.valueOf(score));
                vmsList.add(xyAxisVM);
            }
            xyMap.put(taskId, XYAxisVMUtil.convertList2Vm(vmsList));
        }
    }


    /**
     * 统计待评任务数量
     *
     * @param assessStatus
     * @return
     */
    public long myAssessTaskCount(Byte assessStatus) {
        return assessTaskRepository.countAssessTask(SecurityUtils.getCurrentUserId(), assessStatus);
    }

    /**
     * 查询下属评价列表
     *
     * @param pageable
     */
    public Page<SubordinateAssessTaskDTO> subordinateAssessTaskList(SubordinateAssessTaskDTO dto, Pageable pageable) {
        //查询season
        Long seasonId = dto.getSeasonId();
        Season season = seasonRepository.selectByPrimaryKey(seasonId);
        ExceptionUtil.checkState(season != null, "季度不存在");

        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<Department> leaderDepartments = departmentRepository.findAllByLeaderCode(currentUserId);
        //查询所属部门id集合
        List<Long> departmentIds = Lists.newArrayList();
        for (Department department : leaderDepartments) {
            departmentIds.add(department.getId());
        }

        List<Long> depIdList = departmentService.getDepartmentIdAndChildrens(departmentIds);


        long count = assessTaskRepository.countSubordinateAssessTaskList(seasonId, depIdList, currentUserId, dto.getAssessorRealName(), dto.getUserRealName());
        List<SubordinateAssessTaskDTO> subordinateAssessTaskDTOs = new ArrayList<>();
        if (count > 0) {
            subordinateAssessTaskDTOs = assessTaskRepository.querySubordinateAssessTaskList(seasonId, depIdList, currentUserId, dto.getAssessorRealName(), dto.getUserRealName(), pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            List<Long> taskIdList = subordinateAssessTaskDTOs.stream().map(SubordinateAssessTaskDTO::getId).collect(Collectors.toList());
            Map<Long, XYAxisVM> xyAxisVMMap = queryAssessTaskItemView(taskIdList);
            subordinateAssessTaskDTOs.forEach(at -> {
                at.setXyAxisVM(xyAxisVMMap.get(at.getId()));
            });
            AwsPictureProcessUtil.assembleSubordinateAssessTaskDTO(subordinateAssessTaskDTOs);
        }
        Page<SubordinateAssessTaskDTO> result = new PageImpl(subordinateAssessTaskDTOs, pageable, count);
        return result;
    }


    /**
     * 获取个人信息
     *
     * @param taskId
     * @return
     */
    public UserInfoDTO getDetailForUserInfo(Long taskId) {
        UserInfoDTO userInfoDto = assessTaskRepository.getDetailForUserInfoByTaskId(taskId);
        List<UserInfoDTO> list = Lists.newArrayList(userInfoDto);
        AwsPictureProcessUtil.assembleUserInfoDTO(list);
        return list.get(0);
    }

    /**
     * 获取季度目标
     *
     * @param taskId
     * @return
     */
    public UserOkrDTO getDetailForUserSeason(Long taskId) {
        AssessTask assessTask = assessTaskRepository.selectByPrimaryKey(taskId);
        ExceptionUtil.checkState(assessTask != null, "评价任务不存在");

        Long userSeasonId = assessTask.getUserSeasonId();

        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(userSeasonId);
        Season season = seasonRepository.selectByPrimaryKey(userSeason.getSeasonId());
        return new UserOkrDTO(season.getId(), userSeason.getId(), season.getTitle(), null, null);
    }

    /**
     * 获取获取季度评分
     *
     * @param taskId
     * @return
     */
    public List<UserScoreItemDTO> getDetailForUserScore(Long taskId) {
        AssessTask assessTask = assessTaskRepository.selectByPrimaryKey(taskId);
        ExceptionUtil.checkState(assessTask != null, "评价任务不存在");

        return getScoreItemByTaskId(taskId);
    }


    /**
     * 获取获取季度xy柱状图
     *
     * @param taskId
     * @return
     */
    public AssessTaskViewVM queryTaskXYAxisVM(Long taskId) {
        AssessTaskViewVM viewVM = new AssessTaskViewVM();
        List<AssessTaskItem> list = assessTaskItemRepository.findAllByTaskId(taskId);
        Map<Long, XYAxisVM> xyAxisVMMap = queryAssessTaskItemView(Lists.newArrayList(taskId));
        viewVM.setXyAxisVM(xyAxisVMMap.get(taskId));
        viewVM.setAssessBool(list != null && list.size() > 0);
        return viewVM;
    }

    /**
     * 获取用户所有的季度评分
     *
     * @param userSeasonId
     * @param userSeasonId
     * @return
     */
    public List<List<UserScoreItemDTO>> getUserAllItemScore(Long userSeasonId) {
        List<AssessTask> assessTaskList = assessTaskRepository.findAllByUserSeasonId(userSeasonId);
        List<List<UserScoreItemDTO>> dtoList = new ArrayList<>();
        for (AssessTask task : assessTaskList) {
            List<UserScoreItemDTO> userScoreItemDTOList = getScoreItemByTaskId(task.getId());
            dtoList.add(userScoreItemDTOList);
        }
        return dtoList;
    }

    /**
     * 评分
     *
     * @param vm
     */
    @Transactional
    public CommonResult makeScore(MakeScoreVM vm) {
        AssessTask assessTask = assessTaskRepository.selectByPrimaryKey(vm.getTaskId());
        ExceptionUtil.checkState(assessTask != null, "评价任务不存在");

        //检查是否是自己的评价任务
        ExceptionUtil.checkState(assessTask.getAssessorId().equals(SecurityUtils.getCurrentUserId()), "无权限评价");

        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(assessTask.getUserSeasonId());
        Season season = seasonRepository.selectByPrimaryKey(userSeason.getSeasonId());
        ExceptionUtil.checkState(!season.isMakeSeasonScore(), "评价任务已经失效");

        List<ScoreOption> scoreOptionList = scoreOptionRepository.findAllByType(null);
        Map<Long, ScoreOption> scoreOptionMap = Maps.newHashMap();
        BigDecimal totalScore = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;
        for (ScoreOption scoreOption : scoreOptionList) {
            totalWeight = totalWeight.add(BigDecimal.valueOf(scoreOption.getWeight()));
            scoreOptionMap.put(scoreOption.getId(), scoreOption);
        }

        List<AssessTaskItem> assessTaskItems = new ArrayList<>();
        AssessTaskItem assessTaskItem;
        Map<BigDecimal, Integer> checkScoreMap = Maps.newHashMap();
        for (MakeScoreVM.MakeScoreItem makeScoreItem : vm.getItems()) {
            assessTaskItem = new AssessTaskItem();
            assessTaskItem.setTaskId(vm.getTaskId());
            BigDecimal score = makeScoreItem.getScore();
            assessTaskItem.setScore(score);
            //得分相同计数
            sameScoreCount(checkScoreMap, score);
            assessTaskItem.setOptionId(makeScoreItem.getOptionId());
            ScoreOption scoreOption = scoreOptionMap.get(assessTaskItem.getOptionId());
            totalScore = totalScore.add(BigDecimal.valueOf(scoreOption.getWeight())
                    .divide(totalWeight, 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(assessTaskItem.getScore()));
            assessTaskItems.add(assessTaskItem);
        }
        if (checkSameScoreCount(checkScoreMap)) {
            return CommonResult.error("存在5个及以上相同分数，请合理打分！");
        }
        //清除历史数据
        int deleteCount = assessTaskItemRepository.deleteByTaskId(assessTask.getId());
        if (deleteCount == 0) {
            //累计评论数量
            userReportService.updateAssessCount(assessTask.getAssessorId());
        }

        //保存任务状态和总分
        assessTaskRepository.updateStatusAndScoreById(AssessTaskStatus.FINISHED.getCode(), totalScore, assessTask.getId());
        //保存评分项
        assessTaskItemRepository.insertList(assessTaskItems);
        Map<String, Integer> userRankMap = userRankService.getUserRankMapByRedis();
        //计算评分
        makeScoreAndRankService.makeUserSeasonScore(userSeason, scoreOptionMap, userRankMap);
        return CommonResult.success(MakeScoreCriterionUtil.generatePromptMessage(totalScore));
    }

    /**
     * 相同评分计数
     *
     * @param checkScoreMap
     * @param score
     */
    private void sameScoreCount(Map<BigDecimal, Integer> checkScoreMap, BigDecimal score) {
        Integer count = checkScoreMap.get(score);
        count = null == count ? 0 : count;
        count++;
        checkScoreMap.put(score, count);
    }

    /**
     * 检查相同评分的数量
     *
     * @param checkScoreMap
     * @return
     */
    private boolean checkSameScoreCount(Map<BigDecimal, Integer> checkScoreMap) {
        for (Map.Entry<BigDecimal, Integer> entry : checkScoreMap.entrySet()) {
            if (entry.getValue() >= CommonConstants.NUM_5) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取待评任务存在未评用户信息
     *
     * @return
     */
    public List<UserInfoDTO> queryAssessTaskUserByStatusUnderway(Long seasonId) {
        return assessTaskRepository.queryAssessTaskUserByStatusUnderway(seasonId);
    }

    /**
     * 获取当前评价人的评分明细
     *
     * @param taskId
     * @return
     */
    private List<UserScoreItemDTO> getScoreItemByTaskId(Long taskId) {
        List<ScoreOption> scoreOptions = scoreOptionRepository.findAllByType(null);

        Map<Long, BigDecimal> assessTaskItemMap = assessTaskItemRepository
                .findAllByTaskId(taskId)
                .stream()
                .collect(Collectors.toMap(assessTaskItem -> assessTaskItem.getOptionId(), assessTaskItem -> assessTaskItem.getScore()));

        return scoreOptions
                .stream()
                .map(scoreOption -> {
                    UserScoreItemDTO userScoreDTO = new UserScoreItemDTO();
                    userScoreDTO.setOptionId(scoreOption.getId());
                    userScoreDTO.setOptionName(scoreOption.getName());
                    userScoreDTO.setOptionTypeCode(scoreOption.getType());
                    userScoreDTO.setOptionTypeValue(OptionType.getValueByCode(scoreOption.getType()));
                    userScoreDTO.setScore(assessTaskItemMap.get(scoreOption.getId()));
                    return userScoreDTO;
                })
                .collect(Collectors.toList());
    }

}
