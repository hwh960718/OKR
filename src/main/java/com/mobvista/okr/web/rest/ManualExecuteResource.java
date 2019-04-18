package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.job.MakeAssessTaskJob;
import com.mobvista.okr.job.MakeSeasonScoreJob;
import com.mobvista.okr.job.SyncAcsDepartmentJob;
import com.mobvista.okr.job.SyncAcsUserJob;
import com.mobvista.okr.service.InitService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 注释：手动触发执行
 * 作者：刘腾飞【liutengfei】
 * 时间：2018-03-08 17:29
 */
@RestController
@RequestMapping("/api/u/manual")
public class ManualExecuteResource {

    @Resource
    private MakeAssessTaskJob taskJob;

    @Resource
    private MakeSeasonScoreJob scoreJob;

    @Resource
    private SyncAcsUserJob userJob;

    @Resource
    private SyncAcsDepartmentJob departmentJob;
    @Resource
    private InitService initService;

    /**
     * 生成评价任务
     */
    @ApiOperation(value = "生成评价任务")
    @GetMapping("/makeTask")
    public CommonResult makeTask() {
        taskJob.execute();
        return CommonResult.success("success");
    }

    /**
     * 生成分数和排名
     */
    @ApiOperation(value = "生成分数和排名")
    @GetMapping("/makeScore")
    public CommonResult makeScore() {
        scoreJob.execute();
        return CommonResult.success("success");
    }

    /**
     * 同步部门
     */
    @ApiOperation(value = "同步部门")
    @GetMapping("/syncDept")
    public CommonResult syncDept() {
        departmentJob.execute();
        return CommonResult.success("success");
    }

    /**
     * 同步用户
     */
    @ApiOperation(value = "同步用户")
    @GetMapping("/syncUser")
    public CommonResult syncUser() {
        userJob.execute();
        return CommonResult.success("success");
    }

    @ApiOperation(value = "同步用户")
    @GetMapping("/initUserRank")
    public CommonResult initUserRank() {
        initService.initUserRank();
        return CommonResult.success();
    }
}
