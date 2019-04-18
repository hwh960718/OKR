package com.mobvista.okr.web.rest;

import com.google.common.collect.Lists;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.enums.score.ScoreRuleTypeEnum;
import com.mobvista.okr.enums.score.ScoreTriggerModelEnum;
import com.mobvista.okr.service.score.ScoreRuleService;
import com.mobvista.okr.util.KVUtil;
import com.mobvista.okr.vm.ScoreRuleVM;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/7/24 11:32
 */
@RestController
@RequestMapping("/api/score/scoreRule/")
public class ScoreRuleResource {

    @Resource
    private ScoreRuleService scoreRuleService;

    @ApiOperation(value = "获取所有触发方式")
    @GetMapping("getAllTriggerModel")
    public CommonResult getAllTriggerModel(Byte type) {
        List<KVUtil> list = ScoreTriggerModelEnum.getListByType(ScoreRuleTypeEnum.getEnumByCode(type));
        return CommonResult.success(list);
    }


    @ApiOperation(value = "获取积分类型枚举")
    @GetMapping("getScoreRuleTypeEnum")
    public CommonResult getScoreRuleTypeEnum() {
        List<KVUtil> list = Lists.newArrayList();
        KVUtil kv;
        for (ScoreRuleTypeEnum en : ScoreRuleTypeEnum.values()) {
            kv = new KVUtil();
            kv.setId(Long.valueOf(en.getCode()));
            kv.setName(en.getText());
            list.add(kv);
        }
        return CommonResult.success(list);
    }

    @ApiOperation(value = "保存积分规则")
    @PostMapping("saveScoreRule")
    public CommonResult saveScoreRule(@RequestBody ScoreRuleVM scoreRuleVM) {
        return scoreRuleService.saveScoreRule(scoreRuleVM);
    }

    @ApiOperation(value = "获取相同触发方式的规则")
    @GetMapping("querySameTriggerModeScoreRule")
    public CommonResult querySameTriggerModeScoreRule(Long scoreRuleId) {
        return scoreRuleService.querySameTriggerModeScoreRule(scoreRuleId);
    }


    @ApiOperation(value = "修改积分规则状态")
    @PostMapping("updateScoreRuleStatus")
    public CommonResult updateScoreRuleStatus(@RequestBody ScoreRuleVM scoreRuleVM) {
        if (null == scoreRuleVM) {
            return CommonResult.error("入参为空");
        }
        return scoreRuleService.updateScoreRuleStatus(scoreRuleVM.getId(), scoreRuleVM.getStatus());
    }


    @ApiOperation(value = "分页查询积分规则")
    @GetMapping("queryScoreRulePage")
    public CommonResult queryScoreRulePage(String ruleName, Byte type, Pageable pageable) {
        ScoreRuleVM scoreRuleVM = new ScoreRuleVM();
        scoreRuleVM.setRuleName(ruleName);
        scoreRuleVM.setType(type);
        return scoreRuleService.queryScoreRule(scoreRuleVM, pageable);
    }


//    /**
//     * 获取兑换类有效积分规则
//     *
//     * @return
//     */
//    @GetMapping("queryValidScoreRule/exchange")
//    public CommonResult queryValidScoreRuleExchange() {
//        return scoreRuleService.queryVaildScoreRule(ScoreTriggerModelEnum.Score_Exchange.getCode());
//    }
//
//    /**
//     * 获取竞拍类有效积分规则
//     *
//     * @return
//     */
//    @GetMapping("queryValidScoreRule/auction")
//    public CommonResult queryValidScoreRuleAuction() {
//        return scoreRuleService.queryVaildScoreRule(ScoreTriggerModelEnum.Score_Auction.getCode());
//    }

}
