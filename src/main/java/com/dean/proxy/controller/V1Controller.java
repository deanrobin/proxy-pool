package com.dean.proxy.controller;

import java.util.List;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.cache.ProxyCachePool;
import com.dean.proxy.response.ProxyRes;
import com.dean.proxy.service.operations.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dean
 */
@RequestMapping(value = "/v1/proxy")
@Controller
public class V1Controller {

    @Autowired
    ProxyCachePool pcp;
    @Autowired
    ProxyService proxyService;


    @RequestMapping("/count")
    @ResponseBody
    public Integer count() {
        //return pcp.size();
        return proxyService.usableCount();
    }

    @RequestMapping("/randomGetOne")
    @ResponseBody
    public ProxyRes randomGetOne() {
        Proxy proxy = pcp.randomGetProxy();
        if (proxy == null) {
            return new ProxyRes();
        }
        Integer number = pcp.selectByInternalId(proxy.getInternalId());
        return ProxyRes.transform(proxy, number);
    }

    @RequestMapping("/getList")
    @ResponseBody
    public List<ProxyRes> getList(@RequestParam(value = "type", required = false, defaultValue = "HTTP") String type,
                                  @RequestParam(value = "country", required = false, defaultValue = "中国") String country,
                                  @RequestParam(value = "source", required = false) String source) {
        return proxyService.getList(type, source, country);
    }

    @RequestMapping("/getLastOne")
    @ResponseBody
    public List<ProxyRes> getLastOne(@RequestParam(value = "count", required = false, defaultValue = "1") Integer count) {

        return proxyService.getLast(count);
    }
}
