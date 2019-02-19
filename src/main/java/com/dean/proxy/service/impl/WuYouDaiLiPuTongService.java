package com.dean.proxy.service.impl;

import java.util.List;

import com.dean.proxy.bean.Proxy;
import org.springframework.stereotype.Service;

/**
 * @author dean
 */
@Service
public class WuYouDaiLiPuTongService extends WuYouDaiLiService {

    @Override
    public String getURL() {
        return "http://www.data5u.com/free/gnpt/index.shtml";
    }

    @Override
    public String getProxyId() {
        return "5UP";
    }
}
