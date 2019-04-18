package com.mobvista.okr.enums;

/**
 * 注释：评价结果枚举
 *
 * @author 顾炜[GuWei]
 */
public enum AssessResultEnum {
    VALID((byte) 1, "有效"),
    /**
     * 兼容历史数据
     */
    INVALID((byte) 2, "无效"),
    /**
     * 无效(不属于T1-T3)级别
     */
    INVALID_BELONG_T1_3((byte) 3, "不属于T1-T3"),
    /**
     * 无效(评论人数不满足)
     */
    INVALID_ASSESS_COUNT_NEED((byte) 4, "评论人数不满足"),
    /**
     * 无效(其他部门评价人数不满足)
     */
    INVALID_OTHER_DEPARTMENT_COUNT_NEED((byte) 5, "其他部门评价人数不满足");

    private byte code;
    private String name;

    AssessResultEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据名称获取code
     *
     * @param name
     * @return
     */
    public static Byte getCodeByName(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        AssessResultEnum[] enums = AssessResultEnum.values();
        for (AssessResultEnum typeEnum : enums) {
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
    public static String getNameByCode(Byte code) {
        if (code == null || code == 0) {
            return null;
        }
        AssessResultEnum[] enums = AssessResultEnum.values();
        for (AssessResultEnum typeEnum : enums) {
            if (code.equals(typeEnum.getCode())) {
                return typeEnum.getName();
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
