package com.dean.proxy.service;

import java.util.List;

import com.alibaba.fastjson.JSON;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.db.ProxyMapper;
import com.dean.proxy.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dean
 */
@Service
@Slf4j
public abstract class AbstractProxyService implements ProxyService {

    @Autowired
    HttpClient httpClient;
    @Autowired
    ProxyMapper proxyMapper;

    public final static String COUNTRY = "中国";

    public void reptileProxy() {
        String url = getURL();
        try {
            String html = httpClient.getOkhttpHtml(true, url);

            List<Proxy> list = reptileProxy(html);

            for (Proxy proxy : list) {
                Proxy dbProxy = proxyMapper.getByInternal(proxy.getInternalId());
                if (dbProxy != null) {
                    log.info("this proxy is in DB:" + dbProxy.getInternalId());
                    continue;
                }
                proxy.setTimestamp(System.currentTimeMillis());
                log.info("proxy insert into DB:" + proxy.getInternalId());

                proxy.setType(proxy.getType().toUpperCase());
                proxyMapper.insert(proxy);
            }
        } catch (Exception e) {
            log.error("proxy service have error:" + url, e);
        }

    }
}
