package com.dean.proxy.util;

public class IntegerUtil {

    public static final Integer getBigger(Integer a, Integer b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }


    public static final Integer getSmaller(Integer a, Integer b) {
        if (a < b) {
            return a;
        } else {
            return b;
        }
    }
}
