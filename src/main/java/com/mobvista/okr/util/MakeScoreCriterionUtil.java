package com.mobvista.okr.util;

import com.mobvista.okr.dto.MakeScoreCriterionDTO;
import com.mobvista.okr.enums.MakeScoreCriterionEnum;

import java.math.BigDecimal;

/**
 * 评分标准工具类
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
 * @date 2018/8/24 14:00
 */
public class MakeScoreCriterionUtil {


    /**
     * 根据得分生成提示信息
     *
     * @param score
     * @return
     */
    public static final MakeScoreCriterionDTO generatePromptMessage(BigDecimal score) {
        MakeScoreCriterionDTO dto = new MakeScoreCriterionDTO();
        dto.setScore(score);
        MakeScoreCriterionEnum makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_0;
        if (null == score) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_0;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_10.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_10;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_20.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_20;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_30.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_30;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_40.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_40;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_50.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_50;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_60.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_60;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_70.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_70;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_80.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_80;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_90.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_90;

        } else if (score.compareTo(new BigDecimal(MakeScoreCriterionEnum.NUM_100.getKey())) <= 0) {
            makeScoreCriterionEnum = MakeScoreCriterionEnum.NUM_100;
        }
        dto.setCriterion(makeScoreCriterionEnum.getText());
        dto.setScoreLevelMsg(makeScoreCriterionEnum.getMsg());
        return dto;
    }
}
