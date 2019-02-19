package com.dean.proxy.task;

import com.dean.proxy.service.operations.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dean
 */
@Component
public class VerifyTask {
    @Autowired
    VerifyService verifyService;

    /**
     * 验证是否可用 status从0到1的过程
     */
    @Scheduled(fixedDelay = 10 * 1000)
    public void verifyFirstTime() {
        verifyService.initialValidation();
    }

    /**
     * 验证可用的代理 是否正常可用
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void verifySuccessAgain() {
        verifyService.verifySuccessAgain();
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void verifyNeedAgain() {
        verifyService.verifyNeedVerifyAgain();
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void verifyUnavailable() {
        verifyService.verifyUnavailable();
    }
}
