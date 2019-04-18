package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.domain.Department;
import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.domain.UserReport;
import com.mobvista.okr.domain.UserSeason;
import com.mobvista.okr.dto.SeasonUserDTO;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.dto.mbacs.AccountUserProfile;
import com.mobvista.okr.enums.AssessTaskStatus;
import com.mobvista.okr.enums.UserRankEnum;
import com.mobvista.okr.enums.YesOrNoEnum;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.repository.*;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.service.mbacs.AcsProxyService;
import com.mobvista.okr.util.AwsPictureProcessUtil;
import com.mobvista.okr.util.OKRListUtil;
import com.mobvista.okr.util.OkHttpUtil;
import com.mobvista.okr.vm.UpdateUserVM;
import com.mobvista.okr.vo.AccountPageData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 *
 * @author guwei
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserProfileRepository userProfileRepository;
    @Resource
    private DepartmentRepository departmentRepository;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private RedisService redisService;
    @Resource
    private AcsProxyService acsProxyService;
    @Resource
    private UserReportRepository userReportRepository;
    @Resource
    private UserSeasonRepository userSeasonRepository;

    @Resource
    private AssessTaskRepository assessTaskRepository;

    /**
     * 处理id数组
     *
     * @param jsonStr
     * @return
     */
    public static Long getDepartmentIdFromJson(String jsonStr) {
        if (StringUtils.isNotBlank(jsonStr) && jsonStr.length() > 2) {
            JSONObject idsJson = JSONObject.parseObject(jsonStr);
            Set<String> jobs = idsJson.keySet();
            return Long.parseLong(jobs.toArray()[0].toString());
        }
        return null;
    }

    /**
     * 处理名称数组
     *
     * @param jsonStr
     * @return
     */
    public static String makeJsonName(String jsonStr) {
        if (StringUtils.isNotBlank(jsonStr) && jsonStr.length() > 2) {
            JSONObject namesJson = JSONObject.parseObject(jsonStr);
            Collection<Object> jobs = namesJson.values();
            return Joiner.on(",").join(jobs);
        }
        return null;
    }

    /**
     * 获取登陆人用户信息
     *
     * @return
     */
    public UserInfoDTO getUserInfo() {
        return queryUserInfoById(SecurityUtils.getCurrentUserId());
    }

    /**
     * 根据id查询用户信息
     *
     * @param userId
     * @return
     */
    public UserInfoDTO queryUserInfoById(Long userId) {
        UserProfile userProfile = userProfileRepository.selectByPrimaryKey(userId);
        ExceptionUtil.checkState(null != userProfile, "用户登录信息不存在");
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(userProfile.getId());
        userInfoDTO.setRealName(userProfile.getRealName());
        userInfoDTO.setProfilePhoto(AwsPictureProcessUtil.getPictureUrl(userProfile.getProfilePhoto()));
        userInfoDTO.setEmail(userProfile.getEmail());
        userInfoDTO.setStatus(userProfile.getStatus());
        userInfoDTO.setJobName(userProfile.getJobName());
        userInfoDTO.setRank(userProfile.getRank());
        userInfoDTO.setGender(userProfile.getGender());
        if (userProfile.getLeaderId() != null) {
            UserProfile leaderUser = userProfileRepository.selectByPrimaryKey(userProfile.getLeaderId());
            if (null != leaderUser) {
                userInfoDTO.setLeaderId(leaderUser.getLeaderId());
                userInfoDTO.setLeaderName(leaderUser.getRealName());
            }
        }

        if (userProfile.getDepartmentId() != null) {
            Department department = departmentRepository.selectByPrimaryKey(userProfile.getDepartmentId());
            if (null != department) {
                userInfoDTO.setDepartmentId(department.getId());
                userInfoDTO.setDepartmentName(department.getName());
            }
        }
        UserReport userReport = userReportRepository.findByUserId(userProfile.getId());
        if (null != userReport) {
            userInfoDTO.setReportedCount(userReport.getReportedCount());
            //userInfoDTO.setAssessCount(userReport.getAssessCount());
            //设置评价数量 todo 原处理有问题，只有真正评分的才能计算在内
            long assessCount = assessTaskRepository.countValidAssessTask(userId, AssessTaskStatus.FINISHED.getCode());
            userInfoDTO.setAssessCount((int) assessCount);
        }
        return userInfoDTO;
    }


    /**
     * 修改头像
     *
     * @param imageUrl
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateUserFace(String imageUrl) {
        UserProfile userProfile = userProfileRepository.selectByPrimaryKey(SecurityUtils.getCurrentUserId());
        String oldPhoto = userProfile.getProfilePhoto();
        userProfile.setProfilePhoto(imageUrl);
        userProfileRepository.updateByPrimaryKeySelective(userProfile);
        AwsPictureProcessUtil.deletePicture(oldPhoto, imageUrl);
        log.debug("Changed face for User: {}", userProfile);
    }

    /**
     * 根据用户名、职级、部门名称
     * 模糊查询全部用户信息
     *
     * @param name     用户名、职级、部门名称
     * @param pageable 分页对象
     * @return 用户分页对象
     */
    public Page<UserInfoDTO> findAll(String name, Pageable pageable) {
        List<Long> deptIds = departmentService.getOkrDepartmentIds();
        List<Long> depIds = departmentService.getDepartmentIdAndChildrens(deptIds);
        long count = userProfileRepository.countAllByName(name, depIds);
        List<UserInfoDTO> userInfoDtos = new ArrayList<>();
        if (count > 0) {
            userInfoDtos = userProfileRepository.queryAllByName(name, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize(), depIds);
            boolean canModify = SecurityUtils.getUserOperatorPerm().getCanModifyUserManageUserData().equals(YesOrNoEnum.YES.getCode());
            userInfoDtos.forEach(dto -> dto.setCanModify(canModify));
        }
        Page<UserInfoDTO> result = new PageImpl<>(userInfoDtos, pageable, count);
        return result;
    }


    public Page<UserInfoDTO> findAll(String name, Long seasonId, Pageable pageable) {
        List<Long> deptIds = departmentService.getOkrDepartmentIds();
        List<Long> depIds = departmentService.getDepartmentIdAndChildrens(deptIds);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        long count = userProfileRepository.countAllByAdjusterUser(name, seasonId, currentUserId, depIds);
        List<UserInfoDTO> userInfoDtos = new ArrayList<>();
        if (count > 0) {
            userInfoDtos = userProfileRepository.queryAllByAdjusterUser(name, seasonId, currentUserId, depIds, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            AwsPictureProcessUtil.assembleUserInfoDTO(userInfoDtos);
        }
        Page<UserInfoDTO> result = new PageImpl<>(userInfoDtos, pageable, count);
        return result;
    }

    /**
     * 根据用户userSeasonId 查询用户信息
     *
     * @param name
     * @param userSeasonId
     * @param pageable
     * @return
     */
    public Page<UserInfoDTO> searchByUserSeasonId(String name, Long userSeasonId, Pageable pageable) {
        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(userSeasonId);
        return findAll(name, userSeason.getSeasonId(), pageable);
    }


    /**
     * 根据用户名和用户考核id查询用户信息
     *
     * @param name
     * @param seasonId
     * @param pageable
     * @return
     */
    public Page<SeasonUserDTO> findSeasonUserByNameAndSeasonId(String name, Long seasonId, Pageable pageable) {
        ExceptionUtil.checkState(null != seasonId, "考核信息不能为空");
        int count = userProfileRepository.countAllByUserNameAndSeasonId(name, seasonId);
        List<SeasonUserDTO> dtoList = Lists.newArrayList();
        if (count > 0) {
            dtoList = userProfileRepository.queryAllByUserNameAndSeasonId(name, seasonId, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        }
        return new PageImpl<>(dtoList, pageable, count);
    }

    /**
     * 更新用户信息
     *
     * @param updateUserVM
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult updateUserInfo(UpdateUserVM updateUserVM) {
        try {
            if (SecurityUtils.getUserOperatorPerm().getCanModifyUserManageUserData().equals(YesOrNoEnum.NO.getCode())) {
                return CommonResult.error("无操作权限");
            }
            UserProfile userProfile = userProfileRepository.selectByPrimaryKey(updateUserVM.getUserId());
            if (userProfile == null) {
                return CommonResult.error("用户信息不存在");
            }
            if (StringUtils.isNotBlank(updateUserVM.getWorkplace())) {
                userProfile.setWorkplace(updateUserVM.getWorkplace());
            }
            if (StringUtils.isNotBlank(updateUserVM.getRank())) {
                userProfile.setRank(updateUserVM.getRank());
            }
            if (StringUtils.isNotBlank(updateUserVM.getCollege())) {
                userProfile.setCollege(updateUserVM.getCollege());
            }
            if (updateUserVM.getDegree() == null) {
                userProfile.setDegree(updateUserVM.getDegree());
            }
            if (StringUtils.isNotBlank(updateUserVM.getMajor())) {
                userProfile.setMajor(updateUserVM.getMajor());
            }
            userProfileRepository.updateByPrimaryKeySelective(userProfile);
        } catch (Exception e) {
            log.debug("更新用户信息错误", e);
        }
        return CommonResult.success();
    }


    /**
     * 根据多个部门id获取该部门下的所有人员
     *
     * @param departmentIdList
     * @return
     * @throws Exception
     */
    public List<AccountUserProfile> getAllAccountProfiles(List<Long> departmentIdList) {
        if (null != departmentIdList && departmentIdList.size() > 0) {
            String ids = OKRListUtil.list2String(departmentIdList);
            return getAllAccountProfiles(ids);
        }

        return new ArrayList<>();
    }

    /**
     * 根据多个部门id获取该部门下的所有人员
     *
     * @param departmentIds
     * @return
     * @throws Exception
     */
    public List<AccountUserProfile> getAllAccountProfiles(String departmentIds) {
        try {
            List<AccountUserProfile> allAccountUserProfileList = new ArrayList<>();
            Integer page = 1;
            Integer pageTotal = 1;

            do {
                String uri = acsProxyService.getAcsDeptURIByDeptIds(departmentIds, page);
                String response = OkHttpUtil.get(uri);
                AccountPageData pageData = JSON.parseObject(response, AccountPageData.class);
                page++;
                Integer perPage = pageData.getPerPage();
                Integer total = pageData.getTotal();
                pageTotal = total % perPage == 0 ? total / perPage : total / perPage + 1;
                List<AccountUserProfile> list = pageData.getContents();
                if (list != null && list.size() != 0) {
                    allAccountUserProfileList.addAll(list);
                }
            } while (page <= pageTotal);
            return allAccountUserProfileList;
        } catch (Exception e) {
            log.error("根据多个部门id获取该部门下的所有人员", e);
        }
        return new ArrayList<>();
    }


    /**
     * 获取一级部门下的二级部门对应的人员id（包含一级部门下的人员id）
     *
     * @return
     */
    public Map<Long, List<Long>> getUserIdListByDepartmentId() {
        String value = redisService.get(RedisConstants.ASSESS_GROUP_SECOND_LEVEL_DEPARTMENT_USER_IDS);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, new TypeReference<Map<Long, List<Long>>>() {
            });
        }
        Map<Long, List<Long>> departMap = departmentService.getSecondLevelDepartmentIdListByOneLevel();
        Map<Long, List<Long>> map = getUserIdListByDepartmentIdMap(departMap);
        redisService.set(RedisConstants.ASSESS_GROUP_SECOND_LEVEL_DEPARTMENT_USER_IDS, JSON.toJSONString(map), RedisConstants.EXPIRE_TIME_1H);
        return map;
    }


    /**
     * 获取一级部门下的二级部门对应的人员id（包含一级部门下的人员id）
     *
     * @return
     */
    public Map<Long, List<Long>> getUserIdListByDepartmentIdMap(Map<Long, List<Long>> departMap) {
        ExceptionUtil.checkState(null != departMap && departMap.size() > 0, "部门查询条件为空");
        Map<Long, List<Long>> map = Maps.newHashMap();
        departMap.forEach((oneId, secondIdList) -> {
            map.putAll(getUserIdListByDepartmentIdList(secondIdList));
        });
        return map;
    }

    /**
     * 根据部门id分组显示人员id，包含父级部门对应的人员id
     *
     * @param departIdList
     * @return
     */
    public Map<Long, List<Long>> getUserIdListByDepartmentIdList(List<Long> departIdList) {
        Map<Long, List<Long>> map = Maps.newHashMap();
        departIdList.forEach(departId -> {
            List<Long> newDepartIdList = departmentService.getDepartmentIdAndChildrens(Lists.newArrayList(departId));
            List<UserProfile> userList = userProfileRepository.findAllByDepartmentIdIn(newDepartIdList);
            List<Long> list = map.get(departId);
            list = null == list ? Lists.newArrayList() : list;
            List<Long> userIdList = userList.stream().map(UserProfile::getId).collect(Collectors.toList());
            list.addAll(userIdList);
            map.put(departId, list);
        });

        return map;
    }

    /**
     * 获取有效状态下的部门人员
     *
     * @param accountUserProfileList
     * @return
     * @throws Exception
     */
    private List<AccountUserProfile> getValidAccountProfiles(List<AccountUserProfile> accountUserProfileList) throws Exception {
        if (accountUserProfileList == null || accountUserProfileList.size() == 0) {
            return new ArrayList<>();
        }

        List<AccountUserProfile> validProfiles = new ArrayList<>();
        for (AccountUserProfile profile : accountUserProfileList) {
            if (profile.getStatus() == 10 || profile.getStatus() == 9) {    //10为正常，9为锁定，0为失效，1为未激活。
                validProfiles.add(profile);
            }
        }

        return validProfiles;
    }


    /**
     * 对应职级其他部门需要的评价人数
     */
    public Map<String, Integer> getRankOtherDepartmentNeedCount() {
        Map<String, Integer> map = Maps.newHashMap();

        //重新封装
        for (UserRankEnum userRankEnum : UserRankEnum.values()) {
            String key = userRankEnum.getInfo() + CommonConstants.APPEND_COLON + RedisConstants.RANK_OTHER_DEPARTMENT_NEED_COUNT;
            int count = 0;
            //从缓存获取数据
            String value = redisService.get(key);
            if (StringUtils.isBlank(value)) {
                //设置到redis，缓存1H
                redisService.set(key, String.valueOf(userRankEnum.getOtherDepartmentCount()), RedisConstants.EXPIRE_TIME_1H);
                count = userRankEnum.getOtherDepartmentCount();
            } else {
                count = Integer.valueOf(value);
            }
            map.put(userRankEnum.getInfo(), count);
        }
        return map;
    }


    /**
     * 根据用户id获取对应的部门id
     *
     * @param userId
     * @param map
     * @return
     */
    public List<Long> getUserDepartmentIdFromMapByUserId(Long userId, Map<Long, List<Long>> map) {
        List<Long> departmentIdList = Lists.newArrayList();
        map.forEach((departId, userList) -> {
            if (userList.contains(userId)) {
                departmentIdList.add(departId);
            }
        });
        return departmentIdList;
    }

    /**
     * 根据部门id集合获取对应的用户id集合
     *
     * @param departIdList
     * @param map
     * @return
     */
    public List<Long> getUserIdListFromMapByDepartmentIdList
    (List<Long> departIdList, Map<Long, List<Long>> map) {
        List<Long> userIdList = Lists.newArrayList();
        departIdList.forEach(depId -> {
            List<Long> list = map.get(depId);
            if (null != list && list.size() > 0) {
                userIdList.addAll(list);
            }
        });
        return userIdList;
    }


    /**
     * 获取所有的OKR使用用户邮箱集合
     *
     * @return
     */
    public List<String> queryAllOKRUserEmail() {
        //从redis获取所有用户的邮箱
        String value = redisService.get(RedisConstants.OKR_ALL_USED_USER_EMAIL_KEY);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseArray(value, String.class);
        }
        Set<String> emailSet = Sets.newHashSet();
        //获取所有的部门id
        List<Long> allDepartmentIdList = departmentService.getOkrALLDepartmentIds();
        if (null == allDepartmentIdList || allDepartmentIdList.size() == 0) {
            log.error("获取所有的OKR使用用户邮箱集合----用户所属部门不存在");
            return Lists.newArrayList();
        }
        //根据部门id获取邮箱信息
        List<UserProfile> userList = userProfileRepository.findAllByDepartmentIdIn(allDepartmentIdList);
        for (UserProfile userProfile : userList) {
            emailSet.add(userProfile.getEmail());
        }
        //移除 set中的null对象
        emailSet.remove(null);
        List<String> emailList = Lists.newArrayList(emailSet);
        //设置redis 并设置过期时间
        redisService.set(RedisConstants.OKR_ALL_USED_USER_EMAIL_KEY, JSON.toJSONString(emailList), RedisConstants.EXPIRE_TIME_24H);
        return emailList;
    }

    /**
     * 移除所有的OKR用户邮箱 redis数据
     * 移除当前所有使用OKR系统用户的邮箱缓存
     */
    public void removeAllOkrUserEmailRedisData() {
        redisService.remove(RedisConstants.OKR_ALL_USED_USER_EMAIL_KEY);

    }


}
