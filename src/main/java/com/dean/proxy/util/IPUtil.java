package com.dean.proxy.util;

/**
 * @author dean
 */
public class IPUtil {

    public static String deletePoint(String ip) {
        String[] strs = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String s : strs) {
            sb.append(s);
        }
        return sb.toString();
    }
}
