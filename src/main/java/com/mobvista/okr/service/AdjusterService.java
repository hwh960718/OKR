package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.domain.Adjuster;
import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.domain.UserSeason;
import com.mobvista.okr.dto.UserInfoDTO;
import com.mobvista.okr.dto.UserSeasonDTO;
import com.mobvista.okr.enums.UserRankEnum;
import com.mobvista.okr.enums.UserStatus;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.repository.AdjusterRepository;
import com.mobvista.okr.repository.SeasonRepository;
import com.mobvista.okr.repository.UserProfileRepository;
import com.mobvista.okr.repository.UserSeasonRepository;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.util.AwsPictureProcessUtil;
import com.mobvista.okr.vm.SelectedUsersVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing adjuster.
 *
 * @author guwei
 */
@Service
public class AdjusterService {

    private final Logger log = LoggerFactory.getLogger(AdjusterService.class);

    @Resource
    private UserProfileRepository userProfileRepository;
    @Resource
    private AdjusterRepository adjusterRepository;
    @Resource
    private UserSeasonRepository userSeasonRepository;

    @Resource
    private DepartmentService departmentService;
    @Resource
    private SeasonRepository seasonRepository;

    @Resource
    private RedisService redisService;

    @Resource
    private UserReportService userReportService;

    private static final String ADJUSTER_CREATE_REDIS_KEY = "adjuster_create_%s_%s_%s";

    /**
     * 创建评价人
     *
     * @param selectedUsersVM
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult create(SelectedUsersVM selectedUsersVM) {
        UserSeason us = userSeasonRepository.selectByPrimaryKey(selectedUsersVM.getUserSeasonId());
        Long seasonId = us.getSeasonId();
        String lockKey = String.format(ADJUSTER_CREATE_REDIS_KEY, SecurityUtils.getCurrentUserId(), selectedUsersVM.getSelectedUserId(), seasonId);
        //锁定用户操作，每次只允许添加选中用户一次，防重复提交
        if (!redisService.lock(lockKey, RedisConstants.REDIS_LOCK_EXPIRED_TIME)) {
            return CommonResult.error("选中用户只能添加一次");
        }
        try {

            UserSeason userSeason = userSeasonRepository.findOneBySeasonIdAndUserId(seasonId, selectedUsersVM.getSelectedUserId());
            ExceptionUtil.checkState(userSeason != null, "当前用户不参与考评");

            long count = adjusterRepository.countByAdjusterIdAndSeasonId(SecurityUtils.getCurrentUserId(), seasonId);
            ExceptionUtil.checkState(count < RedisConstants.ADJUSTER_SELECTED_MAX, "考评对象数量超过上限了");

            ExceptionUtil.checkState(!SecurityUtils.getCurrentUserId().equals(selectedUsersVM.getSelectedUserId()), "不能选自己");
            Long userSeasonId = userSeason.getId();
            Adjuster adjusterDb = adjusterRepository.findOneByAdjusterIdAndUserSeasonId(SecurityUtils.getCurrentUserId(), userSeasonId);
            ExceptionUtil.checkState(adjusterDb == null, "已存在此考评对象");

            List<Long> deptIds = departmentService.getOkrDepartmentIds();
            List<Long> childDepIdList = departmentService.getDepartmentIdAndChildrens(deptIds);

            UserProfile userProfile = userProfileRepository.selectByPrimaryKey(selectedUsersVM.getSelectedUserId());
            ExceptionUtil.checkState(childDepIdList.contains(userProfile.getDepartmentId()), "必须选择工程中心或AI中心的同事");

            Adjuster adjuster = new Adjuster();
            adjuster.setUserSeasonId(userSeasonId);
            adjuster.setAdjusterId(SecurityUtils.getCurrentUserId());
            adjusterRepository.insert(adjuster);
            //选择评价数加1
            userReportService.updateAdjusterCount(SecurityUtils.getCurrentUserId(), CommonConstants.NUM_1);
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("创建评价人异常", e);
        } finally {
            redisService.unLock(lockKey);
        }
        return CommonResult.success();
    }

    /**
     * 删除评价人
     *
     * @param selectedUserId
     * @param userSeasonId
     */
    @Transactional
    public void remove(Long selectedUserId, Long userSeasonId) {
        //当前考评人id
        adjusterRepository.deleteByUserIdAndUserSeasonId(selectedUserId, userSeasonId);
        //当前用户选择评价数-1
        userReportService.updateAdjusterCount(SecurityUtils.getCurrentUserId(), CommonConstants.NUM_1_NEGATIVE);
    }


    /**
     * 获取已选择的考评对象
     *
     * @return
     */
    public List<UserInfoDTO> getSelectedUsers(Long userSeasonId, Byte userStatus) {
        List<UserInfoDTO> userInfoDTOList = adjusterRepository.querySelectedUsers(userSeasonId, SecurityUtils.getCurrentUserId(), userStatus);
        AwsPictureProcessUtil.assembleUserInfoDTO(userInfoDTOList);
        return userInfoDTOList;
    }

    /**
     * 获取所有选择的考评对象
     *
     * @return
     */
    public List<UserInfoDTO> getAllSelectedUsers() {
        List<UserInfoDTO> userInfoDTOList = adjusterRepository.querySelectedUsers(null, SecurityUtils.getCurrentUserId(), UserStatus.NORMAL.getCode());
        AwsPictureProcessUtil.assembleUserInfoDTO(userInfoDTOList);
        return userInfoDTOList;
    }

    /**
     * 根据职级获取对应的数量
     *
     * @param rank
     * @return
     */
    public int getAdjusterCountFromRedisByRankContains(String rank) {
        UserRankEnum userRankEnum = UserRankEnum.getEnumByDetailContains(rank);
        if (null == userRankEnum) {
            return 0;
        }
        return getAdjusterCountFromRedisByFlag(userRankEnum);
    }


    /**
     * 根据标识获取对应级别考评用户数
     *
     * @param userRankEnum
     * @return
     */
    public int getAdjusterCountFromRedisByFlag(UserRankEnum userRankEnum) {
        String key = getUserRankRedisKey(userRankEnum);
        String value = redisService.get(key);
        //若redis取值为空，返回默认值
        int count = userRankEnum.getAdjusterCount();
        if (StringUtils.isNotBlank(value)) {
            count = Integer.parseInt(value);
        }
        return count;
    }


    /**
     * 获取所有职级对应的需要评价的人数量
     */
    public void getAllRankAdjusterCount() {
        for (UserRankEnum userRankEnum : UserRankEnum.values()) {
            int count = getAdjusterCountFromRedisByFlag(userRankEnum);
            log.info("获取职级对应的需要评价的人数量-->职级：{}，需要评价人数量：{}", userRankEnum.getInfo(), count);
        }
    }


    /**
     * 根据用户考核id 获取用户选择考评人，被选中数量
     *
     * @return
     */
    public UserSeasonDTO getUserAdjustInfoByUserSeasonId(Long userSeasonId) {
        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(userSeasonId);
        ExceptionUtil.checkState(null != userSeason, "用户考核信息为空");
        UserSeasonDTO dto = new UserSeasonDTO();
        if (null == userSeason) {
            return dto;
        }
        //获取用户被选择数量 排除无效用户
        long adjustedCount = adjusterRepository.countByUserSeasonIdExceptInvalidUser(userSeason.getId());
        dto.setAdjustedCount(adjustedCount);
        //评价数量
        long adjustCount = adjusterRepository.countByAdjusterIdAndSeasonId(userSeason.getUserId(), userSeason.getSeasonId());
        dto.setAdjustCount(adjustCount);
        //需要被评价的数量
        UserProfile userProfile = userProfileRepository.selectByPrimaryKey(userSeason.getUserId());
        if (null != userProfile && StringUtils.isNotBlank(userProfile.getRank())) {
            String key = getUserRankRedisKey(UserRankEnum.getEnumByDetailContains(userProfile.getRank()));
            String value = redisService.get(key);
            if (StringUtils.isNotBlank(value)) {
                dto.setAdjustedNeedCount(Long.valueOf(value));
            }
        }
        return dto;
    }

    public Map<String, Integer> getAllUserRankAdjusterCount() {
        Map<String, Integer> map = Maps.newHashMap();
        for (UserRankEnum userRankEnum : UserRankEnum.values()) {
            String key = getUserRankRedisKey(userRankEnum);
            String value = redisService.get(key);
            if (StringUtils.isNotBlank(value)) {
                map.put(userRankEnum.getInfo(), Integer.valueOf(value));
            }
        }
        return map;


    }


    private String getUserRankRedisKey(UserRankEnum userRankEnum) {
        String key = "";
        if (null != userRankEnum) {
            switch (userRankEnum) {
                case T1:
                    key = RedisConstants.T1_ADJUSTER_COUNT_KEY;
                    break;
                case T2:
                    key = RedisConstants.T2_ADJUSTER_COUNT_KEY;
                    break;
                case T3:
                    key = RedisConstants.T3_ADJUSTER_COUNT_KEY;
                    break;
                default:
                    break;
            }
        }
        return key;
    }


    /**
     * 从redis获取百分比配置
     *
     * @return
     */
    public BigDecimal getFinishAssessMinScaleByRedisKey() {
        String value = redisService.get(RedisConstants.FINISH_ASSESS_MIN_SCALE_KEY);
        if (StringUtils.isNotBlank(value)) {
            return BigDecimal.valueOf(Double.valueOf(value)).setScale(2);
        }
        //默认设置为1
        return BigDecimal.ONE;
    }


    /**
     * 导入历史考核评价人
     *
     * @param seasonId
     */
    public CommonResult importHistorySeasonAdjuster(Long seasonId) {

        //获取所有历史选择用户

        List<UserInfoDTO> historyUserList = getAllSelectedUsers();
        if (null == historyUserList || historyUserList.size() == 0) {
            return CommonResult.error("历史选择考评人不存在");
        }
        Long currentUserId = SecurityUtils.getCurrentUserId();
        //当前选择评价人
        List<Long> idList = adjusterRepository.findUserIdByAdjusterIdAndSeasonId(currentUserId, seasonId);
        List<Adjuster> adjusterList = new ArrayList<>();
        //历史选择评级人id
        List<Long> historyUserIdList = historyUserList.stream().map(UserInfoDTO::getId).collect(Collectors.toList());
        //历史评价人考核信息是否存在判断
        List<UserSeason> userSeasonList = userSeasonRepository.findOneBySeasonIdAndUserIdIn(seasonId, historyUserIdList);
        if (null == userSeasonList || userSeasonList.size() == 0) {
            return CommonResult.error("导入历史考评用户失败，用户考核信息不存在。");
        }
        Map<Long, Long> userSeasonMap = userSeasonList.stream().collect(Collectors.toMap(UserSeason::getUserId, UserSeason::getId));
        for (UserInfoDTO userInfoDTO : historyUserList) {
            try {
                Long userId = userInfoDTO.getId();
                if (idList.contains(userId)) {
                    continue;
                }
                Long userSeasonId = userSeasonMap.get(userId);
                if (null == userSeasonId) {
                    continue;
                }
                Adjuster adjuster = new Adjuster();
                adjuster.setUserSeasonId(userSeasonId);
                adjuster.setAdjusterId(currentUserId);
                adjusterList.add(adjuster);
            } catch (Exception e) {
                log.error("导入上一次考评用户失败{}。", JSON.toJSONString(userInfoDTO), e);
            }
        }
        if (adjusterList.size() > 0) {
            adjusterRepository.insertList(adjusterList);
            //选择评价数增加
            userReportService.updateAdjusterCount(currentUserId, adjusterList.size());
        }
        return CommonResult.success(true);
    }
}
