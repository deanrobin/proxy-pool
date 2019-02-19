package com.dean.proxy.constant;

public enum  AnonymityEnum {
    TRANSPARENT(0),
    NORMAL(1),
    HIGHLY(2);

    private int code;

    AnonymityEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static int getCode(String str) {
        if (str.contains("高")) {
            return HIGHLY.code;
        }
        if (str.contains("透明")) {
            return TRANSPARENT.code;
        }
        return NORMAL.code;
    }

}
