package com.dean.proxy;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.db.ProxyMapper;
import com.dean.proxy.service.AbstractProxyService;
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
public class ProxyListTest {
    private static Logger log = LoggerFactory.getLogger(DBTest.class);

    @Autowired
    HttpClient httpClient;
    @Autowired
    ProxyPoolContext context;
    @Autowired
    ProxyMapper proxyMapper;

    @Test
    public void insert() {
        log.info("test begin!!");
        log.error("error test");
    }

    @Test
    public void testHtml() {
        AbstractProxyService kuaiDaiLiService = (AbstractProxyService)context.getInstance("6BJ");
        kuaiDaiLiService.reptileProxy();
    }

    @Test
    public void httpClient() {
        System.out.println(
            httpClient.getOkhttpHtml(true, "https://www.baidu.com", "", 9999));
    }

    @Test
    public void testUpdate() {
        Proxy proxy = new Proxy();
        proxy.setId(31);
        proxy.setStatus(0);
        System.out.println(proxyMapper.updateStatus(proxy));
    }
}
