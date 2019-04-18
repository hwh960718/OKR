package com.mobvista.okr.enums;

/**
 * 注释：是否枚举
 * 作者：刘腾飞【liutengfei】
 * 时间：2017-03-06 下午5:49
 */
public enum YesOrNoEnum {
    YES(1, "是"), NO(2, "否");

    private Integer code;
    private String name;

    private YesOrNoEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据名称获取code
     *
     * @param name
     * @return
     */
    public static Integer getCodeByName(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        YesOrNoEnum[] enums = YesOrNoEnum.values();
        for (YesOrNoEnum typeEnum : enums) {
            if (name.equals(typeEnum.getName())) {
                return typeEnum.getCode();
            }
        }
        return null;
    }

    /**
     * 根据code获取名称
     *
     * @param code
     * @return
     */
    public static String getNameByCode(Integer code) {
        if (code == null || code <= 0) {
            return null;
        }
        YesOrNoEnum[] enums = YesOrNoEnum.values();
        for (YesOrNoEnum typeEnum : enums) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getName();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Integer getCodeByBool(boolean bool) {
        return bool ? YES.code : NO.code;
    }
}
