package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.SeasonService;
import com.mobvista.okr.service.UserSeasonService;
import com.mobvista.okr.vm.SeasonVM;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/u/season")
public class SeasonResource {

    private final Logger log = LoggerFactory.getLogger(SeasonResource.class);

    @Resource
    private SeasonService seasonService;

    @Resource
    private UserSeasonService userSeasonService;

    @ApiOperation(value = "创建季度考核")
    @PostMapping("/create")
    public CommonResult create(@Valid @RequestBody SeasonVM seasonVM) {
        log.debug("REST request to create season: {}", seasonVM);
        try {
            return CommonResult.success(seasonService.create(seasonVM));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[创建季度考核]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除季度考核")
    @GetMapping(value = "/delete")
    public CommonResult remove(Long id) {
        log.debug("REST request to remove season id: {}", id);
        try {
            seasonService.remove(id);
            return CommonResult.success();
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[删除季度考核]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "修改季度考核")
    @PostMapping("/update")
    public CommonResult update(@Valid @RequestBody SeasonVM seasonVM) {
        log.debug("REST request to update season: {}", seasonVM);
        try {
            return CommonResult.success(seasonService.update(seasonVM));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[修改季度考核]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "发布季度考核")
    @GetMapping(value = "/publish")
    public CommonResult publish(Long id) {
        log.debug("REST request to publish id: {}", id);
        try {
            seasonService.publish(id);
            return CommonResult.success();
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[发布季度考核]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "重新发布季度考核")
    @GetMapping(value = "/rePublish")
    public CommonResult rePublish(Long id) {
        log.debug("REST rePublish to rePublish id: {}", id);
        seasonService.rePublish(id);
        return CommonResult.success();
    }

//    @ApiOperation(value = "获取季度考核")
//    @GetMapping(value = "/detail")
//    public CommonResult detail(Long id) {
//        log.debug("REST request to get season: {}", id);
//        try {
//            return CommonResult.success(seasonService.findById(id));
//        } catch (ServiceErrorException e) {
//            return CommonResult.error(e.getMessage());
//        }
//    }

    @ApiOperation(value = "分页季度考核")
    @GetMapping("/list")
    public CommonResult list(Pageable pageable) {
        log.debug("REST request to page season: {}", pageable);
        return CommonResult.success(seasonService.findList(pageable));
    }

//    @ApiOperation(value = "获取当前季度考核")
//    @GetMapping(value = "/getCurrentSeason")
//    public CommonResult getCurrentSeason() {
//        log.debug("REST request to getCurrentSeason");
//        return CommonResult.success(seasonService.getCurrentSeason());
//    }

    @ApiOperation(value = "获取当前季度考核列表")
    @GetMapping(value = "/getCurrentSeasons")
    public CommonResult getCurrentSeasons() {
        log.debug("REST request to getCurrentSeasons");
        return CommonResult.success(seasonService.getCurrentSeasons());
    }

    @ApiOperation(value = "获取所有年度")
    @GetMapping(value = "/getAllYears")
    public CommonResult getAllSeasonYears() {
        log.debug("REST request to getAllSeasonYears");
        return CommonResult.success(seasonService.getAllSeasonYears());
    }

    @ApiOperation(value = "获取年度所有季度")
    @GetMapping(value = "/getAllSeasonsByYear")
    public CommonResult getAllSeasonsByYear(@RequestParam String year) {
        log.debug("REST request to getAllSeasonsByYear : {}", year);
        return CommonResult.success(seasonService.getAllSeasonsByYear(year));
    }

    @ApiOperation(value = "获取没有完成OKR目标的人员列表")
    @GetMapping(value = "/getUnFinishFilledOkrList")
    public CommonResult getUnFinishFilledOkrList(@RequestParam Long seasonId, Pageable pageable) {
        log.debug("REST request to getUnFinishFilledOkrList");
        return CommonResult.success(userSeasonService.getUnFinishFilledOkrList(seasonId, pageable));
    }

//    @ApiOperation(value = "获取未完成选人的员工")
//    @GetMapping(value = "/getUnSelectedAssessorList")
//    public CommonResult getUnSelectedAssessorList(@RequestParam Long seasonId, Pageable pageable) {
//        log.debug("REST request to getUnSelectedAssessorList");
//        return CommonResult.success(userSeasonService.getUnSelectedAssessorList(seasonId, pageable));
//    }

    @ApiOperation(value = "获取未完成评价任务的员工")
    @GetMapping(value = "/getUnFinishAssessList")
    public CommonResult getUnFinishAssessList(@RequestParam Long seasonId, Pageable pageable) {
        log.debug("REST request to getUnFinishAssessList");
        return CommonResult.success(userSeasonService.getUnFinishAssessList(seasonId, pageable));
    }


    @ApiOperation(value = "获取用户考核统计信息")
    @GetMapping(value = "/querySeasonUserInfoCount")
    public CommonResult querySeasonUserInfoCount(@RequestParam Long seasonId) {
        return userSeasonService.querySeasonUserInfoCount(seasonId);
    }
}
