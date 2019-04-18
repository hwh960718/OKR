package com.mobvista.okr.enums.score;

import com.google.common.collect.Lists;
import com.mobvista.okr.util.KVUtil;

import java.util.List;

/**
 * 积分渠道枚举
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 11:28
 */
public enum ScoreTriggerModelEnum {
    //完成评价
    Finish_Access((byte) 1, "完成评价", ScoreRuleTypeEnum.Grant),
    //选定未评价
    Selected_unAccess((byte) 2, "选定未评价", ScoreRuleTypeEnum.Penalty),
    //评价被投诉
    Access_Report((byte) 3, "评价被投诉", ScoreRuleTypeEnum.Penalty),
    //评价投诉确认
    Access_Report_Sure((byte) 4, "评价投诉确认", ScoreRuleTypeEnum.Reward);
//    //积分兑换
//    Score_Exchange((byte) 4, "积分兑换", ScoreRuleTypeEnum.Abatement),
//    Score_Auction((byte) 5, "积分竞拍", ScoreRuleTypeEnum.Abatement);


    private Byte code;

    private String value;

    private ScoreRuleTypeEnum scoreRuleTypeEnum;

    ScoreTriggerModelEnum(Byte code, String value, ScoreRuleTypeEnum scoreRuleTypeEnum) {
        this.code = code;
        this.value = value;
        this.scoreRuleTypeEnum = scoreRuleTypeEnum;
    }

    public Byte getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public ScoreRuleTypeEnum getScoreRuleTypeEnum() {
        return scoreRuleTypeEnum;
    }

    public static List<KVUtil> getListByType(ScoreRuleTypeEnum scoreRuleTypeEnum) {
        List<KVUtil> list = Lists.newArrayList();
        KVUtil kvUtil;
        for (ScoreTriggerModelEnum modelEnum : ScoreTriggerModelEnum.values()) {
            if (scoreRuleTypeEnum.equals(modelEnum.getScoreRuleTypeEnum())) {
                kvUtil = new KVUtil();
                kvUtil.setId(Long.valueOf(modelEnum.getCode()));
                kvUtil.setName(modelEnum.getValue());
                list.add(kvUtil);
            }
        }
        return list;
    }

    public static String getValueByCode(Byte code) {
        if (null != code) {
            for (ScoreTriggerModelEnum en : ScoreTriggerModelEnum.values()) {
                if (en.getCode().equals(code)) {
                    return en.getValue();
                }
            }
        }
        return null;
    }
}
