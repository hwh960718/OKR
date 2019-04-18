package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.service.RedisManageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * redis 管理后台接口
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/2 10:35
 */
@RestController
@RequestMapping("/api/manage/redis")
public class RedisManageResource {

    @Resource
    private RedisManageService redisManageService;

    @ApiOperation(value = "获取需要评论的数量")
    @PostMapping(value = "/queryAssessedUserCountInfo")
    public CommonResult queryAssessedUserCountInfo() {
        return redisManageService.queryAssessedUserCountInfo();
    }


    @ApiOperation(value = "修改需要考评的数量")
    @PostMapping(value = "/updateAssessedUserCount")
    public CommonResult updateAssessedUserCount(String key, Integer count) {
        return redisManageService.updateAssessedUserCount(key, count);
    }


    @ApiOperation(value = "获取需要其他部门评论的数量")
    @PostMapping(value = "/queryAssessedOtherDepUserCountInfo")
    public CommonResult queryAssessedOtherDepUserCountInfo() {
        return redisManageService.queryAssessedOtherDepUserCountInfo();
    }


    @ApiOperation(value = "修改需要评价的其他部门的数量")
    @PostMapping(value = "/updateAssessedOtherDepUserCount")
    public CommonResult updateAssessedOtherDepUserCount(String key, Integer count) {
        return redisManageService.updateAssessedOtherDepUserCount(key, count);
    }


    @ApiOperation(value = "获取OKR参与考核的部门ID")
    @PostMapping(value = "/getOkrDepartmentIds")
    public CommonResult getOkrDepartmentIds() {
        return redisManageService.getOkrDepartmentIds();
    }


    @ApiOperation(value = "修改获取OKR参与考核的部门ID")
    @PostMapping(value = "/updateOkrDepartmentIds")
    public CommonResult updateOkrDepartmentIds(String deptIds) {
        return redisManageService.updateOkrDepartmentIds(deptIds);
    }


    @ApiOperation(value = "获取考评用户分组 一级部门id集合")
    @PostMapping(value = "/queryAssessGroupDepartmentOneLevel")
    public CommonResult queryAssessGroupDepartmentOneLevel() {
        return redisManageService.queryAssessGroupDepartmentOneLevel();
    }


    @ApiOperation(value = "更新用户一级部门分组")
    @PostMapping(value = "/updateAssessGroupDepartmentOneLevel")
    public CommonResult updateAssessGroupDepartmentOneLevel(String deptIds) {
        return redisManageService.updateAssessGroupDepartmentOneLevel(deptIds);
    }

    @ApiOperation(value = "获取考评用户分组 二级部门id下用户")
    @PostMapping(value = "/queryAssessGroupDepartmentTwoLevelUser")
    public CommonResult queryAssessGroupDepartmentTwoLevelUser() {
        return redisManageService.queryAssessGroupDepartmentTwoLevelUser();
    }
}
