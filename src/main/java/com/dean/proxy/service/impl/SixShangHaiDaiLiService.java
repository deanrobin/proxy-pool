package com.dean.proxy.service.impl;

import org.springframework.stereotype.Service;

/**
 * @author dean
 */
@Service
public class SixShangHaiDaiLiService extends SixBeiJingDaiLiService {

    @Override
    public String getURL() {
        return "http://www.66ip.cn/areaindex_2/1.html";
    }

    @Override
    public String getProxyId() {
        return "6SH";
    }
}
