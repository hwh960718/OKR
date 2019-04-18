package com.mobvista.okr.util;

import com.mobvista.okr.vm.XYAxisVM;

import java.math.BigDecimal;
import java.util.List;

/**
 * x y 轴工具类
 *
 * @author Weier Gu (顾炜)
 * @date 2018/8/22 17:18
 */
public class XYAxisVMUtil {

    /**
     * 当数据长度超过4条数据时，展示
     */
    public static final int sizeShow = 4;


    /**
     * 将xy list转为xy轴对象
     *
     * @param xyList
     * @return
     */
    public static final XYAxisVM convertList2Vm(List<XYAxisVM> xyList) {
        XYAxisVM vm = new XYAxisVM();
        if (null == xyList || xyList.size() == 0) {
            return vm;
        }
        StringBuffer x = new StringBuffer();
        StringBuffer y = new StringBuffer();
        for (XYAxisVM axisVM : xyList) {
            x = StringOkrUtil.appendStringBuffer(x, axisVM.getX());
            y = StringOkrUtil.appendStringBuffer(y, axisVM.getY());
        }
        vm.setX(x.toString());
        vm.setY(y.toString());
        return vm;
    }


    /**
     * 获取y轴的差值
     *
     * @param xyList
     * @return
     */
    public static final Integer getYDiffInteger(List<XYAxisVM> xyList) {
        //判断数据是否存在
        if (null == xyList || xyList.size() == 0) {
            return 0;
        }
        int size = xyList.size();
        XYAxisVM last = xyList.get(size - 1);
        //判断数据是否只有一条
        if (size == 1) {
            return Integer.valueOf(last.getY());
        }
        XYAxisVM previous = xyList.get(size - 2);
        return Integer.valueOf(last.getY()) - Integer.valueOf(previous.getY());
    }


    /**
     * 获取y轴的差值
     *
     * @param xyList
     * @return
     */
    public static final BigDecimal getYDiffBigDecimal(List<XYAxisVM> xyList) {
        //判断数据是否存在
        if (null == xyList || xyList.size() == 0) {
            return BigDecimal.ZERO;
        }
        int size = xyList.size();
        XYAxisVM last = xyList.get(size - 1);
        //判断数据是否只有一条
        if (size == 1) {
            return BigDecimal.valueOf(Double.valueOf(last.getY()));
        }
        XYAxisVM previous = xyList.get(size - 2);
        return BigDecimal.valueOf(Double.valueOf(last.getY())).subtract(BigDecimal.valueOf(Double.valueOf(previous.getY())));
    }
}
