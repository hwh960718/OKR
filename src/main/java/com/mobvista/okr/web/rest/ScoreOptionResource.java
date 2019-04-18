package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.enums.OptionType;
import com.mobvista.okr.service.ScoreOptionService;
import com.mobvista.okr.vm.UpdateOptionWeightVM;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 评分项接口
 *
 * @author guwei
 */
@RestController
@RequestMapping("/api/u/scoreOption")
public class ScoreOptionResource {
    private final Logger log = LoggerFactory.getLogger(ScoreOptionResource.class);

    @Resource
    private ScoreOptionService scoreOptionService;

    @ApiOperation(value = "获取所有评分项")
    @GetMapping(value = "/all")
    public CommonResult getAllOptions() {
        log.debug("REST request to get getAllOptions: {}");
        return CommonResult.success(scoreOptionService.getAllOptions());
    }

    @ApiOperation(value = "获取评分项配置")
    @GetMapping("/list")
    public CommonResult getOptions(@RequestParam String optionType) {
        log.debug("REST request to get scoreOption: {}");
        return CommonResult.success(scoreOptionService.findListByType(OptionType.getCodeByName(optionType)));
    }

    @ApiOperation(value = "修改评分项配置权重")
    @PostMapping("/update")
    public CommonResult updateOptionWeight(@RequestBody UpdateOptionWeightVM vm) {
        log.debug("REST request to update option weight: {}", vm);
        scoreOptionService.updateWeight(vm.getId(), vm.getWeight());
        return CommonResult.success();
    }
}
