package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.OkrContentService;
import com.mobvista.okr.vm.OkrContentVM;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 注释：okr目标
 * 作者：刘腾飞【liutengfei】
 * 时间：2018-03-30 14:50
 */
@RestController
@RequestMapping("/api/u/okr")
public class OkrContentResource {
    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(OkrContentResource.class);

    @Resource
    private OkrContentService okrContentService;

    @ApiOperation(value = "创建okr")
    @PostMapping(value = "/create")
    public CommonResult create(@Valid @RequestBody OkrContentVM vm) {
        return CommonResult.success(okrContentService.create(vm));
    }

    @ApiOperation(value = "修改okr")
    @PostMapping(value = "/update")
    public CommonResult update(@RequestBody OkrContentVM vm) {
        return okrContentService.update(vm);
    }

    @ApiOperation(value = "删除okr")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam Long id) {
        try {
            okrContentService.delete(id);
            return CommonResult.success();
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[删除okr]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取okr列表")
    @GetMapping(value = "/list")
    public CommonResult list(@RequestParam Long userSeasonId) {
        return CommonResult.success(okrContentService.list(userSeasonId));
    }

}
