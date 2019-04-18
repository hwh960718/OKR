package com.mobvista.okr.service;

import com.google.common.collect.Lists;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.dto.RedisDTO;
import com.mobvista.okr.enums.UserRankEnum;
import com.mobvista.okr.util.OKRListUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * redis管理
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/2 10:58
 */
@Service
public class RedisManageService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(RedisManageService.class);

    @Resource
    public RedisService redisService;


    /**
     * 获取需要评论的数量
     *
     * @return
     */
    public CommonResult queryAssessedUserCountInfo() {
        List<RedisDTO> list = Lists.newArrayList();
        RedisDTO redisDTO;
        for (UserRankEnum userRankEnum : UserRankEnum.values()) {
            redisDTO = new RedisDTO();
            String key = userRankEnum.getInfo().toLowerCase() + CommonConstants.APPEND_COLON + RedisConstants.ADJUSTER_COUNT_KEY;
            redisDTO.setKey(key);
            redisDTO.setKeyText(userRankEnum.getInfo());
            String value = redisService.get(key);
            redisDTO.setAssessedUserCount(Integer.valueOf(value));
            list.add(redisDTO);
        }
        return CommonResult.success(list);
    }


    /**
     * 修改需要考评的数量
     *
     * @param key
     * @param count
     * @return
     */
    public CommonResult updateAssessedUserCount(String key, Integer count) {
        //判断可以是否存在
        List<String> keyList = Lists.newArrayList();
        for (UserRankEnum userRankEnum : UserRankEnum.values()) {
            keyList.add(userRankEnum.getInfo().toLowerCase() + CommonConstants.APPEND_COLON + RedisConstants.ADJUSTER_COUNT_KEY);
        }

        if (!keyList.contains(key)) {
            CommonResult.error("未知的key");
        }

        redisService.set(key, String.valueOf(count));
        return CommonResult.success();
    }


    /**
     * 获取需要其他部门评论的数量
     *
     * @return
     */
    public CommonResult queryAssessedOtherDepUserCountInfo() {
        List<RedisDTO> list = Lists.newArrayList();
        RedisDTO redisDTO;
        for (UserRankEnum userRankEnum : UserRankEnum.values()) {
            redisDTO = new RedisDTO();
            String key = userRankEnum.getInfo() + CommonConstants.APPEND_COLON + RedisConstants.RANK_OTHER_DEPARTMENT_NEED_COUNT;
            redisDTO.setKey(key);
            redisDTO.setKeyText(userRankEnum.getInfo());
            String value = redisService.get(key);
            redisDTO.setAssessedOtherDepUserCount(Integer.valueOf(value));
            list.add(redisDTO);
        }
        return CommonResult.success(list);
    }


    /**
     * 修改需要评价的其他部门的数量
     *
     * @param key
     * @param count
     * @return
     */
    public CommonResult updateAssessedOtherDepUserCount(String key, Integer count) {
        List<String> keyList = Lists.newArrayList();
        for (UserRankEnum userRankEnum : UserRankEnum.values()) {
            keyList.add(userRankEnum.getInfo() + CommonConstants.APPEND_COLON + RedisConstants.RANK_OTHER_DEPARTMENT_NEED_COUNT);
        }
        if (!keyList.contains(key)) {
            return CommonResult.error("未知的key");
        }
        redisService.set(key, String.valueOf(count));
        return CommonResult.success();
    }


    /**
     * 获取OKR参与考核的部门ID
     *
     * @return
     */
    public CommonResult getOkrDepartmentIds() {
        String deptIds = redisService.get(RedisConstants.USE_OKR_DEPARTMENT_IDS);
        return CommonResult.success(deptIds);
    }


    /**
     * 修改获取OKR参与考核的部门ID
     *
     * @param deptIds
     * @return
     */
    public CommonResult updateOkrDepartmentIds(String deptIds) {
        //验证数据格式是否合理
        if (StringUtils.isBlank(deptIds)) {
            return CommonResult.error("参与考核的部门不能为空");
        }
        List<Long> list = OKRListUtil.string2List(deptIds);
        if (null == list) {
            return CommonResult.error("入参格式有误");
        }
        redisService.set(RedisConstants.USE_OKR_DEPARTMENT_IDS, deptIds);
        return CommonResult.success();

    }


    /**
     * 获取考评用户分组 一级部门id集合
     *
     * @return
     */
    public CommonResult queryAssessGroupDepartmentOneLevel() {
        String value = redisService.get(RedisConstants.ASSESS_GROUP_ONE_LEVEL_DEPARTMENT_IDS);
        return CommonResult.success(value);
    }


    /**
     * 更新用户一级部门分组
     *
     * @param deptIds
     * @return
     */
    public CommonResult updateAssessGroupDepartmentOneLevel(String deptIds) {
        if (StringUtils.isBlank(deptIds)) {
            return CommonResult.error("分组的部门不能为空");
        }
        List<Long> list = OKRListUtil.string2List(deptIds);
        if (null == list) {
            return CommonResult.error("入参格式有误");
        }
        redisService.set(RedisConstants.ASSESS_GROUP_ONE_LEVEL_DEPARTMENT_IDS, deptIds);
        return CommonResult.success();
    }

    /**
     * 获取考评用户分组 二级部门id下用户
     *
     * @return
     */
    public CommonResult queryAssessGroupDepartmentTwoLevelUser() {
        String value = redisService.get(RedisConstants.ASSESS_GROUP_SECOND_LEVEL_DEPARTMENT_USER_IDS);
        return CommonResult.success(value);
    }


}
