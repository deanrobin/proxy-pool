package com.dean.proxy.okhttp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Task {
    static int num = 1;

    Cache<String, Integer> cache = CacheBuilder.newBuilder().expireAfterWrite(120, TimeUnit.SECONDS).build();

    @Autowired
    C c;

    @Autowired
    B b;

    @Autowired
    A a;


    ////@Scheduled(cron = "10 * * * * *")
    //public void addd() {
    //    OkHttpClient okHttpClient = new OkHttpClient();
    //    okHttpClient.dispatcher().setMaxRequestsPerHost(20);
    //
    //    StopWatch stopWatch = StopWatch.createStarted();
    //    try {
    //        JSONObject jsonObject = new ThreadPoolTest().aa(okHttpClient);
    //
    //
    //        JSONArray array = jsonObject.getJSONArray("transactions");
    //
    //        array.parallelStream().forEach(obj -> {
    //            JSONObject json = (JSONObject) obj;
    //            String txID = json.getString("txID");
    //
    //            //System.out.println(cache.getIfPresent(txID));
    //
    //            if (cache.getIfPresent(txID) == null) {
    //                cache.put(txID, 1);
    //                JSONObject param = new JSONObject();
    //                param.put("value", txID);
    //
    //                try {
    //                    new ThreadPoolTest().bb(okHttpClient, param, num, stopWatch);
    //                } catch (IOException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        });
    //
    //        stopWatch.stop();
    //        log.info("this task over int:{}, time:{}", String.valueOf(num), String.valueOf(stopWatch.getTime()));
    //
    //        num = num + 1;
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //}

    //@Scheduled(cron = "10 * * * * *")
    public void addd() {
        System.out.println(c.getNumber());
        System.out.println(b.getNumber());

        System.out.println("-------");
        c.addNum();
        System.out.println(c.getNumber());
        System.out.println(b.getNumber());
        System.out.println(",,,,,,");

        b.addNum();

        System.out.println(b.getNumber());
        System.out.println(c.getNumber());

        System.out.println(a.getNum());
    }
}
