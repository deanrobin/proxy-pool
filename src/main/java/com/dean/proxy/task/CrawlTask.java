package com.dean.proxy.task;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dean.proxy.ProxyPoolContext;
import com.dean.proxy.service.AbstractProxyService;
import com.dean.proxy.service.ProxyService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dean
 */
@Component
@Slf4j
public class CrawlTask {
    @Value("${crawl.thead.pool.core}")
    int corePoolSize;
    @Value("${crawl.thead.pool.max}")
    int maxPoolSize;

    @Autowired
    ProxyPoolContext context;

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void crawl() {
        log.info("start crawl task, threadPool core and max is:" + corePoolSize + " ," + maxPoolSize);

        Map<String, ProxyService> map = context.getServiceMap();

        CountDownLatch countDownLatch = new CountDownLatch(map.size());
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("crawl-pool-%d").build();
        ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue(map.size()), namedThreadFactory);

        for (Map.Entry<String, ProxyService> entry : map.entrySet()) {
            AbstractProxyService proxyService = (AbstractProxyService)entry.getValue();

            Runnable task = () -> {
                try {
                    proxyService.reptileProxy();
                }catch (Exception e){
                    log.error("crawl exception " + entry.getKey() + " :{}", e.getMessage(), e);
                } finally {
                    countDownLatch.countDown();
                }
            };
            threadPoolExecutor.execute(task);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("crawl task over " + System.currentTimeMillis());
        threadPoolExecutor.shutdown();
    }
}
