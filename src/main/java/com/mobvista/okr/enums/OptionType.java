package com.mobvista.okr.enums;

public enum OptionType {

    /**
     * 价值与能力
     */
    ACTUALIZE((byte) 1, "价值与能力"),
    /**
     * 态度与为人
     */
    ATTITUDE((byte) 2, "态度与为人");

    private byte code;
    private String value;

    OptionType(byte code, String value) {
        this.code = code;
        this.value = value;
    }

    public byte getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }


    public static String getValueByCode(byte code) {
        for (OptionType optionType : OptionType.values()) {
            if (optionType.getCode() == code) {
                return optionType.getValue();
            }
        }
        return null;
    }

    public static Byte getCodeByName(String name) {
        for (OptionType optionType : OptionType.values()) {
            if (optionType.name().equals(name)) {
                return optionType.getCode();
            }
        }
        return null;
    }
}
