package com.dean.proxy.netty;

public class Test {

    public static void main(String[] args) {
        String str = "1111";
        //String a = "111";
        String str2 = "111" + "1";
        String str3 = "111" + "1";
        System.out.println(str == str2);
        System.out.println(str2 == str3);
    }
}
