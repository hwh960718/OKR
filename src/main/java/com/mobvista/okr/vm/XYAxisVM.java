package com.mobvista.okr.vm;

/**
 * x y 轴对象
 *
 * @author Weier Gu (顾炜)
 * @date 2018/8/22 17:12
 */
public class XYAxisVM {

    /**
     * 是否显示
     */
    private Boolean show = true;

    /**
     * x轴
     */
    private String x;
    /**
     * x轴 排序标识
     */
    private Long xSortFlag;
    /**
     * y轴
     */
    private String y;
    /**
     * y轴 排序标识
     */
    private Long ySortFlag;

    /**
     * y轴差值
     */
    private String yDiff;
    /**
     * 是否 y 正数
     */
    private boolean yDiffPositive;


    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public Long getxSortFlag() {
        return xSortFlag;
    }

    public void setxSortFlag(Long xSortFlag) {
        this.xSortFlag = xSortFlag;
    }

    public Long getySortFlag() {
        return ySortFlag;
    }

    public void setySortFlag(Long ySortFlag) {
        this.ySortFlag = ySortFlag;
    }

    public String getyDiff() {
        return yDiff;
    }

    public void setyDiff(String yDiff) {
        this.yDiff = yDiff;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public boolean isyDiffPositive() {
        return yDiffPositive;
    }

    public void setyDiffPositive(boolean yDiffPositive) {
        this.yDiffPositive = yDiffPositive;
    }
}
