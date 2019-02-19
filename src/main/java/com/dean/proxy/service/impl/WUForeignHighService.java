package com.dean.proxy.service.impl;

import org.springframework.stereotype.Service;

@Service
public class WUForeignHighService extends WuYouDaiLiService {

    @Override
    public String getURL() {
        return "http://www.data5u.com/free/gwgn/index.shtml";
    }

    @Override
    public String getProxyId() {
        return "5UF";
    }
}
