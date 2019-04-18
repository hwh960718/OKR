package com.mobvista.okr.vm;

import java.math.BigDecimal;
import java.util.List;

/**
 * 雷达图对象
 *
 * @author Weier Gu (顾炜)
 * @date 2018/8/27 11:23
 */
public class RadarVM {

    /**
     * 名字
     */
    private String name;

    /**
     * ACTUALIZE 价值与能力
     */
    private List<BigDecimal> actualize;
    private List<String> actualizeOptionItem;

    /**
     * ATTITUDE 态度与为人
     */
    private List<BigDecimal> attitude;
    private List<String> attitudeOptionItem;

    /**
     * 排序
     */
    private int sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BigDecimal> getActualize() {
        return actualize;
    }

    public void setActualize(List<BigDecimal> actualize) {
        this.actualize = actualize;
    }

    public List<BigDecimal> getAttitude() {
        return attitude;
    }

    public void setAttitude(List<BigDecimal> attitude) {
        this.attitude = attitude;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<String> getActualizeOptionItem() {
        return actualizeOptionItem;
    }

    public void setActualizeOptionItem(List<String> actualizeOptionItem) {
        this.actualizeOptionItem = actualizeOptionItem;
    }

    public List<String> getAttitudeOptionItem() {
        return attitudeOptionItem;
    }

    public void setAttitudeOptionItem(List<String> attitudeOptionItem) {
        this.attitudeOptionItem = attitudeOptionItem;
    }
}
