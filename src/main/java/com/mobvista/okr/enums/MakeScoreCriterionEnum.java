package com.mobvista.okr.enums;

/**
 * 评分枚举
 * <p>
 * 0~10：非常不认可，很多事情背道而驰，完全不符合这个level的水平；
 * 10~20：诸多方面事情上都做不到，在所在级别人员中属于倒数10%人员；
 * 20~30：很难找到达标的事情，不少事情不仅没有达标，而且程度比较严重；
 * 30~40：很难找到表现达标的事情，但是在没有达标的程度上没有特别负面；
 * 40~50：虽然有一些时候的表现达到要求，但是大部分事情上都不符合；
 * 50~60：刚好不合格，有些关键时候或者有些重要事情上会不符合；
 * 60~70：刚好合格，总体表现平庸，有些时候或者有些事情上会不符合；
 * 70~80：达到标准，不是非常纠结，但比较普通，客观事情上的亮点不多且没有非常突出；
 * 80~90：诸多方面事情上都能够保持比较突出，在所在级别人员中属于突出类（TOP 10%）人员；
 * 90~100：在所能看到的事情上都表现突出，无论是公司内外很难看到做的更好的人；
 *
 * @author Weier Gu (顾炜)
 * @date 2018/8/24 14:14
 */
public enum MakeScoreCriterionEnum {
    /**
     *
     */
    NUM_0(0, "未给用户评分", ""),
    NUM_10(10, "0~10：非常不认可，很多事情背道而驰，完全不符合这个level的水平；", "不认可，能力完全不符合职级需要"),
    NUM_20(20, "10~20：诸多方面事情上都做不到，在所在级别人员中属于倒数10%人员；", "能力严重不满足职级需要"),
    NUM_30(30, "20~30：很难找到达标的事情，不少事情不仅没有达标，而且程度比较严重；", "能力很差，程度比较严重"),
    NUM_40(40, "30~40：很难找到表现达标的事情，但是在没有达标的程度上没有特别负面；", "能力较差，影响不严重"),
    NUM_50(50, "40~50：虽然有一些时候的表现达到要求，但是大部分事情上都不符合；", "能力欠缺，不满足职级需要"),
    NUM_60(60, "50~60：刚好不合格，有些关键时候或者有些重要事情上会不符合；", "能力欠缺，关键时候表现能力不符"),
    NUM_70(70, "60~70：刚好合格，总体表现平庸，有些时候或者有些事情上会不符合；", "刚好合格，表现平庸"),
    NUM_80(80, "70~80：达到标准，不是非常纠结，但比较普通，客观事情上的亮点不多且没有非常突出；", "达到标准，比较普通"),
    NUM_90(90, "80~90：诸多方面事情上都能够保持比较突出，在所在级别人员中属于突出类（TOP 10%）人员；", "比较突出"),
    NUM_100(100, "90~100：在所能看到的事情上都表现突出，无论是公司内外很难看到做的更好的人；", "表现突出，无人能及");


    private int key;

    private String text;

    private String msg;


    MakeScoreCriterionEnum(int key, String text, String msg) {
        this.key = key;
        this.text = text;
        this.msg = msg;
    }

    public int getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public String getMsg() {
        return msg;
    }
}
