package com.dean.proxy.task;

import java.text.SimpleDateFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dean.proxy.service.operations.VerifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dean
 */
@Component
@Slf4j
public class VerifyTask {
    @Autowired
    VerifyService verifyService;

    /**
     * 验证是否可用 status从0到1的过程
     */
    //@Scheduled(fixedDelay = 10 * 1000)
    public void verifyFirstTime() {
        StopWatch sw = StopWatch.createStarted();
        verifyService.initialValidation();
        sw.stop();
        log.info("verify first time task, run time:" + sw.getTime());
    }

    ThreadPoolExecutor nofiyOnceExecutor = new ThreadPoolExecutor(
        5, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    /**
     * 验证可用的代理 是否正常可用
     * status=1
     */
    //@Scheduled(fixedDelay = 30 * 1000)
    public void verifySuccessAgain() {
        verifyService.verifySuccessAgain();
    }

    // status=10
    //@Scheduled(fixedDelay = 60 * 1000)
    public void verifyNeedAgain() {
        verifyService.verifyNeedVerifyAgain();
    }

    // status=12
    //@Scheduled(fixedDelay = 5 * 60 * 1000)
    public void verifyUnavailable() {
        verifyService.verifyUnavailable();
    }

    static boolean s = true;
    static int i = 1;

    public static String SDF = "yyyy-MM-dd HH:mm:ss";

    //@Scheduled(fixedDelay =  5 * 1000L)
    private void sss() {
        System.out.println("beig");
        CountDownLatch countDownLatch = new CountDownLatch(50);


        for (int i =0 ; i< 50 ; ++i) {
            final String s = String.valueOf(i);
            Runnable task = () -> {
                try {
                    System.out.println(",," + s);
                    return;
                }catch (Exception e){

                } finally {
                    countDownLatch.countDown();
                }
            };
            nofiyOnceExecutor.execute(task);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //nofiyOnceExecutor.shutdown();
        System.out.println("over");
    }
}
