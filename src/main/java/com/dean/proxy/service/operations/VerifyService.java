package com.dean.proxy.service.operations;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.bean.Verification;
import com.dean.proxy.constant.StatusEnum;
import com.dean.proxy.db.ProxyMapper;
import com.dean.proxy.db.VerificationMapper;
import com.dean.proxy.util.HttpClient;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author dean
 */
@Service
@Slf4j
public class VerifyService {

    @Autowired
    ProxyMapper proxyMapper;
    @Autowired
    VerificationMapper verificationMapper;
    @Autowired
    HttpClient httpClient;

    @Value("${crawl.thead.pool.core}")
    int corePoolSize;
    @Value("${crawl.thead.pool.max}")
    int maxPoolSize;

    @Value("${proxy.abandon.number}")
    int ABANDON;

    String defaultUrl = "http://www.baidu.com";

    final static Long ONE_MIN = 60 * 1000L;



    /**
     *   初次验证 status从 0 --> 1
     *              or -->10
     *   因为根据经验，大多情况 初始获取到的 是最有有效的 所以加快这里 这里使用多线程处理
     *   其他地方 为了节省资源 单线程处理即可
     */
    public void initialValidation() {
        List<Proxy> list = proxyMapper.getNeedVerify();
        if (list == null) {
            log.error("proxyMapper query need verify fail");
            return;
        }

        if(list.size() == 0) {
            log.info("no proxy need verify");
            return;
        }

        log.info("initialValidation list size" + list.size());
        CountDownLatch countDownLatch = new CountDownLatch(list.size());
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("verifyF-pool-%d").build();
        // 线程池 加大，尽快完成验证
        ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(corePoolSize + 10, maxPoolSize + 10, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue(list.size()), namedThreadFactory);

        log.info("begin initialValidation...");
        for (Proxy proxy : list) {

            Runnable task = () -> {
                try {
                    Long timestamp = System.currentTimeMillis();
                    boolean result = updateProxy(proxy, timestamp);

                    if (!result) {
                        log.error("this proxy record update error: " + JSON.toJSON(proxy));
                    } else {
                        insertVerifiaction(proxy.getInternalId(), timestamp,
                            proxy.getStatus() == StatusEnum.VERIFIED.getCode());
                    }
                }catch (Exception e){
                    log.error("initialValidation exception " + JSON.toJSON(proxy), e);
                } finally {
                    countDownLatch.countDown();
                }
            };
            threadPoolExecutor.execute(task);
        }

        log.info("verify initial task over." + System.currentTimeMillis());
    }

    public void verifySuccessAgain() {
        log.info("verify success task begin:" + System.currentTimeMillis());
        List<Proxy> list = proxyMapper.getSuccessVerify();

        if (list == null) {
            log.error("proxyMapper query success verify fail");
            return;
        }

        if(list.size() == 0) {
            log.info("no proxy success verify");
            return;
        }

        for (Proxy proxy : list) {
            Long timestamp = System.currentTimeMillis();
            boolean result = updateProxy(proxy, timestamp);

            if (!result) {
                log.error("this proxy update error:" + JSON.toJSON(proxy));
                continue;
            }

            if (proxy.getStatus() == StatusEnum.VERIFIED.getCode()) {
                continue;
            }

            Verification verification = verificationMapper.getSuccessByInternalId(proxy.getInternalId());
            verification.setUnavailableTime(timestamp);
            verification.setDuration(timestamp - verification.getVerifyAvailableTime());
            verification.setUse(false);

            verificationMapper.update(verification);
        }

        log.info("verify success task end:" + System.currentTimeMillis());
    }


    public void verifyNeedVerifyAgain() {
        log.info("verify need verify task begin:" + System.currentTimeMillis());
        List<Proxy> list = proxyMapper.getNeedVerifyAgain();

        if (list == null) {
            log.error("proxyMapper query need verify again fail");
            return;
        }

        if(list.size() == 0) {
            log.info("no proxy need verify again");
            return;
        }

        for (Proxy proxy : list) {
            Long timestamp = System.currentTimeMillis();
            // 验证时间 大于1分钟 重试
            if (timestamp - proxy.getValidateTime() < ONE_MIN) {
                continue;
            }
            boolean result = updateProxy(proxy, timestamp, StatusEnum.VERIFICATION_UNAVAILABLE.getCode());

            if (!result) {
                log.error("this proxy update error:" + JSON.toJSON(proxy));
                continue;
            }

            if (proxy.getStatus() != StatusEnum.VERIFIED.getCode()) {
                continue;
            }

            Verification verification = new Verification(proxy.getInternalId(), true, timestamp);
            verificationMapper.insert(verification);
        }

        log.info("verify need verify task end:" + System.currentTimeMillis());
    }

    public void verifyUnavailable() {
        log.info("verify unavailable begin:" + System.currentTimeMillis());
        List<Proxy> list = proxyMapper.getUnavailableVerify();

        if (list == null) {
            log.error("proxyMapper query Unavailable verify fail");
            return;
        }

        if(list.size() == 0) {
            log.info("no proxy Unavailable verify");
            return;
        }

        for (Proxy proxy : list) {
            Long timestamp = System.currentTimeMillis();
            boolean result = httpClient.verifyProxy(
                false, defaultUrl, proxy.getIp(), proxy.getPort());
            log.info("proxy verify is " + result + " ,proxy:" + proxy.getInternalId());
            if (result) {
                proxy.setStatus(StatusEnum.VERIFIED.getCode());
                proxy.setAvailableCount(proxy.getAvailableCount() + 1);
                proxy.setContinuousUnavailableNumber(0);
            } else {
                int count = proxy.getContinuousUnavailableNumber() + 1;
                proxy.setContinuousUnavailableNumber(count);
                proxy.setStatus(count > ABANDON ?
                    StatusEnum.ABANDON.getCode() : StatusEnum.VERIFICATION_UNAVAILABLE.getCode());
            }

            proxy.setValidateTime(timestamp);
            proxy.setValidateCount(proxy.getValidateCount() + 1);
            result = proxyMapper.updateStatus(proxy);

            if (!result) {
                log.error("this proxy update error:" + JSON.toJSON(proxy));
                continue;
            }

            if (proxy.getStatus() != StatusEnum.VERIFIED.getCode()) {
                continue;
            }

            Verification verification = new Verification(proxy.getInternalId(), true, timestamp);
            verificationMapper.insert(verification);
        }
        log.info("verify unavailable end:" + System.currentTimeMillis());
    }


    private void insertVerifiaction(String internalId, Long time, boolean use) {
        Verification verification = new Verification(internalId, use, time);

        verificationMapper.insert(verification);
    }

    private boolean updateProxy(Proxy proxy, Long timestamp, Integer status) {
        Integer offset = 0;
        if (proxy.getCountry() == null || !proxy.getCountry().equals("中国")) {
            offset = 5;
        }
        boolean result = httpClient.verifyProxy(
            false, defaultUrl, proxy.getIp(), proxy.getPort(), offset);
        log.info("proxy verify is " + result + " ,proxy:" + proxy.getInternalId());
        if (result) {
            proxy.setStatus(StatusEnum.VERIFIED.getCode());
            proxy.setAvailableCount(proxy.getAvailableCount() + 1);
        } else {
            proxy.setStatus(status);
        }

        proxy.setValidateTime(timestamp);
        proxy.setValidateCount(proxy.getValidateCount() + 1);
        return proxyMapper.updateStatus(proxy);
    }

    private boolean updateProxy(Proxy proxy, Long timestamp) {
        return updateProxy(proxy, timestamp, StatusEnum.NEED_VERIFY_AGAIN.getCode());
    }

}
