package com.dean.proxy.service.impl;

import org.springframework.stereotype.Service;

@Service
public class SixHongKongDaiLiService extends SixBeiJingDaiLiService {

    @Override
    public String getURL() {
        return "http://www.66ip.cn/areaindex_33/1.html";
    }

    @Override
    public String getProxyId() {
        return "6HK";
    }

}
