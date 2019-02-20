package com.dean.proxy.controller;

import com.dean.proxy.cache.ProxyCachePool;
import com.dean.proxy.service.operations.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dean
 */
@RequestMapping(value = "/internal/proxy")
@Controller
public class InternalController {

    @Autowired
    ProxyService proxyService;


    @RequestMapping("/count")
    @ResponseBody
    public Integer count() {
        //return pcp.size();
        return proxyService.count();
    }

    @RequestMapping("/unverifiedCount")
    @ResponseBody
    public Integer unverifiedCount() {
        //return pcp.size();
        return proxyService.unverifiedCount();
    }
}
