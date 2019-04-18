package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.UserReportService;
import com.mobvista.okr.vm.UserReportDetailVM;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 顾炜(GUWEI)
 * @date 2018/5/30 15:53
 */
@RestController
@RequestMapping("/api/u/userReport")
public class UserReportResource {

    @Resource
    private UserReportService userReportService;


    @ApiModelProperty(name = "查询举报明细")
    @GetMapping("/queryList")
    public CommonResult queryList(String reportUserName, Pageable pageable) {

        return CommonResult.success(userReportService.queryList(reportUserName, pageable));
    }

    @ApiModelProperty(name = "举报创建")
    @PostMapping("/createUserReportDetail")
    public CommonResult createUserReportDetail(@RequestBody UserReportDetailVM vm) {
        try {
            return CommonResult.success(userReportService.createUserReportDetail(vm));
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }

    @ApiModelProperty(name = "举报审核通过")
    @PostMapping("/verifyUserReport")
    public CommonResult verifyUserReport(@RequestBody UserReportDetailVM vm) {
        try {
            userReportService.updateReportCount(vm.getId(), vm.getStatus());
            return CommonResult.success();
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 评价数排行榜
     *
     * @return
     * @version v3.0
     */
    @GetMapping("/queryAssessCountInfo")
    public CommonResult queryAssessCountInfo() {
        try {
            return userReportService.queryAssessCountInfo();
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 选择价数排行榜
     *
     * @return
     * @version v3.0
     */
    @GetMapping("/queryAdjusterCountInfo")
    public CommonResult queryAdjusterCountInfo() {
        try {
            return userReportService.queryAdjusterCountInfo();
        } catch (ServiceErrorException e) {
            return CommonResult.error(e.getMessage());
        }
    }

}
