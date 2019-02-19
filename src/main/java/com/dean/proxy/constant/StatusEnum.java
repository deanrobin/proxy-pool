package com.dean.proxy.constant;

/**
 * @author dean
 */
public enum StatusEnum {
    INIT(0),
    VERIFIED(1),
    NEED_VERIFY_AGAIN(10),
    @Deprecated
    VERIFICATION_FIRST_FAILED(11),
    VERIFICATION_UNAVAILABLE(12),
    ABANDON(13);

    private int code;

    StatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
