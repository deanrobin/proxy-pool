package com.dean.proxy.okhttp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class B {

    @Autowired
    A a;

    public int getNumber() {
        return a.getNum();
    }


    public int addNum() {
        a.add();
        return a.getNum();
    }

}
