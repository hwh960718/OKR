package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.service.UserSeasonService;
import com.mobvista.okr.util.StringOkrUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户对比接口
 *
 * @author Weier Gu (顾炜)
 * @date 2018/6/26 15:59
 */
@RestController
@RequestMapping("/api/u/userSeasonCompare")
public class UserSeasonCompareResource {

    @Resource
    private UserSeasonService userSeasonService;


    @ApiOperation(value = "查询用户对比明细")
    @GetMapping(value = "/getUserCompareDetail")
    public CommonResult getUserCompareDetail(String ids) {
        return userSeasonService.getUserSeasonCompareDetail(StringOkrUtil.convertStr2LongList(ids));
    }
}
