package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.service.TipService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 标签功能
 *
 * @author Weier Gu (顾炜)
 * @date 2018/6/29 11:14
 */
@RestController
@RequestMapping("/api/u/tip")
public class TipResource {

    @Resource
    private TipService tipService;


    @ApiOperation(value = "标签自动补全查询")
    @GetMapping("/list")
    public CommonResult list(String title) {
        return tipService.findLikeTitle(title);
    }
}
