package com.mobvista.okr.constants;

import java.math.BigDecimal;

/**
 * 任务&评分 常量设置
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/16 14:04
 */
public class TaskScoreConstants {

    /**
     * 分母
     */
    public static final BigDecimal DENOMINATOR = new BigDecimal(3);

    /**
     * 精度
     */
    public static final int SCALE = 2;

    /**
     * 其他组织机构百分比
     */
    public static final BigDecimal OTHER_DEPARTMENT_PERCENT = BigDecimal.ONE.divide(DENOMINATOR, SCALE, BigDecimal.ROUND_HALF_DOWN);

}
