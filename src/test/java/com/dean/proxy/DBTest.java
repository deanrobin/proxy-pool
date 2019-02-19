package com.dean.proxy;

import com.alibaba.fastjson.JSON;

import com.dean.proxy.db.VerificationMapper;
import com.dean.proxy.service.impl.KuaiDaiLiService;
import com.dean.proxy.util.HttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DBTest {
    private static Logger log = LoggerFactory.getLogger(DBTest.class);

    @Autowired
    HttpClient httpClient;
    @Autowired
    ProxyPoolContext context;
    @Autowired
    VerificationMapper verificationMapper;

    @Test
    public void insert() {
        log.info("test begin!!");
        log.error("error test");

        //System.out.println(JSON.toJSON(
        //    verificationMapper.getSuccessByInternalId("KDL11312824230")
        //));
    }

    @Test
    public void testHtml() {
        KuaiDaiLiService kuaiDaiLiService = (KuaiDaiLiService)context.getInstance("KD");
        kuaiDaiLiService.reptileProxy();
    }
}
