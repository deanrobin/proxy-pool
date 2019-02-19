package com.dean.proxy.service;

import java.util.List;

import com.dean.proxy.bean.Proxy;

/**
 * @author dean
 */
public interface ProxyService {

    String getURL();

    List<Proxy> reptileProxy(String html);

    String getProxyId();
}
