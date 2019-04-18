package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.service.AdjusterService;
import com.mobvista.okr.service.UserRankService;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 顾炜(GUWEI)
 * @date 2018/5/15 14:07
 */
@RestController
@RequestMapping("/api/u/userRank")
public class UserRankResource {
    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(UserRankResource.class);
    @Resource
    private UserRankService userRankService;
    @Resource
    private AdjusterService adjusterService;

    @ApiModelProperty(name = "获取用户职级列表")
    @GetMapping("/getUserRankList")
    public CommonResult getUserRankList() {
        return CommonResult.success(userRankService.getUserRankInfo());
    }


    @ApiModelProperty(name = "修改用户职级对应的权重")
    @GetMapping("/updateWeightById")
    public CommonResult updateUserRankWeight(Long userRankId, Integer weight) {
        if (null == userRankId || userRankId <= 0) {
            return CommonResult.error();
        }
        return CommonResult.success(userRankService.updateUserRank(userRankId, weight));
    }

//    @ApiModelProperty(name = "获取用户归属职级评论人数列表")
//    @GetMapping("/getUserRankAdjusterCountList")
//    public CommonResult getUserRankAdjusterCountList() {
//        return CommonResult.success(adjusterService.getAllRankAscriptionAdjusterCountList());
//    }

//    @ApiModelProperty(name = "修改用户职级对应的评定人数量")
//    @PostMapping("/updateUserRankAdjusterCount")
//    public CommonResult updateUserRankAdjusterCount(@RequestBody UserRankDTO userRankDTO) {
//        adjusterService.updateRankAscriptionAdjusterCount(userRankDTO.getAscription(), userRankDTO.getAdjusterCount());
//        return CommonResult.success();
//    }


}
