package com.mobvista.okr.vm;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/8/29 20:31
 */
public class AssessTaskViewVM {

    private XYAxisVM xyAxisVM;

    /**
     * 是否评价
     */
    private boolean assessBool;

    public XYAxisVM getXyAxisVM() {
        return xyAxisVM;
    }

    public void setXyAxisVM(XYAxisVM xyAxisVM) {
        this.xyAxisVM = xyAxisVM;
    }

    public boolean isAssessBool() {
        return assessBool;
    }

    public void setAssessBool(boolean assessBool) {
        this.assessBool = assessBool;
    }
}
