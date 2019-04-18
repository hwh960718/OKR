package com.mobvista.okr.enums.score;

/**
 * 积分关联标识
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/25 14:53
 */
public enum ScoreRelatedFlagEnum {
    //用户考核
    AssessTask("AssessTask", "用户考核"),
    //用户举报明细
    UserReportDetail("UserReportDetail", "用户举报明细"),
    //举报审核确认
    UserReportSure("UserReportSure", "举报审核确认"),
    ProductOrder("ProductOrder", "商品订单");

    private String code;

    private String Text;

    ScoreRelatedFlagEnum(String code, String text) {
        this.code = code;
        Text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return Text;
    }
}
