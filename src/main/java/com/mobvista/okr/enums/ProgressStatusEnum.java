package com.mobvista.okr.enums;

/**
 * 注释：评价进度枚举
 * 作者：刘腾飞【liutengfei】
 * 时间：2018-04-09 下午2:49
 * @author Weier Gu (顾炜)
 */
public enum ProgressStatusEnum {
    /**
     * 未开始
     */
    NOT_START(0, "未开始"),
    /**
     * 填写OKR和选择评价对象
     */
    FIRST_IN(1, "填写OKR和选择评价对象"),
    /**
     * 填写OKR和选择评价对象阶段结束
     */
    FIRST_OUT(2, "填写OKR和选择评价对象阶段结束"),
    /**
     * 评价考核对象
     */
    SECOND_IN(3, "评价考核对象"),
    /**
     * 考核结束
     */
    END(4, "考核结束");

    private Integer code;
    private String name;

    private ProgressStatusEnum(Integer code, String name) {
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
        ProgressStatusEnum[] enums = ProgressStatusEnum.values();
        for (ProgressStatusEnum typeEnum : enums) {
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
        if (code == null || code == 0) {
            return null;
        }
        ProgressStatusEnum[] enums = ProgressStatusEnum.values();
        for (ProgressStatusEnum typeEnum : enums) {
            if (code.equals(typeEnum.getCode())) {
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
}
