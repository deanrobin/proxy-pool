package com.dean.proxy.okhttp;

import org.springframework.stereotype.Service;

@Service
public class A {

    private int num = 0;

    public int getNum() {
        return num;
    }

    public int add() {
        num = num + 1;
        return num;
    }
}
