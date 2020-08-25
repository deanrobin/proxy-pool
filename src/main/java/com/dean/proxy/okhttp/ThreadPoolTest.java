package com.dean.proxy.okhttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

public class ThreadPoolTest {
    private static final AtomicInteger FINISH_COUNT = new AtomicInteger(0);

    private static final AtomicLong COST = new AtomicLong(0);

    private static final Integer INCREASE_COUNT = 1000000;

    private static final Integer TASK_COUNT = 1000;

    @Test
    public void testRunWithoutThreadPool() {
        List<Thread> tList = new ArrayList<Thread>(TASK_COUNT);

        for (int i = 0; i < TASK_COUNT; i++) {
            tList.add(new Thread(new IncreaseThread()));
        }

        for (Thread t : tList) {
            t.start();
        }

        for (;;);
    }

    @Test
    public void testRunWithThreadPool() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

        for (int i = 0; i < TASK_COUNT; i++) {
            executor.submit(new IncreaseThread());
        }

        for (;;);
    }

    private class IncreaseThread implements Runnable {

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            AtomicInteger counter = new AtomicInteger(0);
            for (int i = 0; i < INCREASE_COUNT; i++) {
                counter.incrementAndGet();
            }
            // 累加执行时间
            COST.addAndGet(System.currentTimeMillis() - startTime);
            if (FINISH_COUNT.incrementAndGet() == TASK_COUNT) {
                System.out.println("cost: " + COST.get() + "ms");
            }
        }

    }

    public JSONObject aa(OkHttpClient okHttpClient) throws IOException {
        //StopWatch stopWatch = StopWatch.createStarted();
        //RequestBody requestBody = new MultipartBody.Builder()
        //    .setType(MultipartBody.FORM)
        //    .addFormDataPart("num", "100")
        //    .build();

        //RequestBody requestBody = new FormBody.Builder()
        //    .add("num", "14887951")
        //    .build();


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        String jsonStr = "{\"num\": 14887951}";//json数据.
        RequestBody body = RequestBody.create(JSON, jsonStr);

        Request request = new Request.Builder()
            .post(body)
            .url("http://34.220.77.106:8091/walletsolidity/getblockbynum")
            .build();
        Call call = okHttpClient.newCall(request);
        ////1.异步请求，通过接口回调告知用户 http 的异步执行结果
        //call.enqueue(new Callback() {
        //    @Override
        //    public void onFailure(Call call, IOException e) {
        //        System.out.println("error");
        //        System.out.println(e.getMessage());
        //    }
        //    @Override
        //    public void onResponse(Call call, Response response) throws IOException {
        //        if (response.isSuccessful()) {
        //            System.out.println(response.body().string());
        //        }
        //        response.close();
        //    }
        //});

        Response response = call.execute();
        //if (response.isSuccessful()) {
        //    System.out.println(response.body().string());
        //}


        //stopWatch.stop();
        //System.out.println(stopWatch.getTime());

        return JSONObject.parseObject(response.body().string());
    }

    public void bb(OkHttpClient okHttpClient, JSONObject jsonObject, Integer num) throws IOException {
        //StopWatch stopWatch = StopWatch.createStarted();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toJSONString());

        Request request = new Request.Builder()
            .post(body)
            .url("http://34.220.77.106:8091/walletsolidity/gettransactionbyid")
            .build();
        Call call = okHttpClient.newCall(request);

        //1.异步请求，通过接口回调告知用户 http 的异步执行结果
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //System.out.println("error");
                System.out.println(e.getMessage());

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println(response.body().string());

                } else {
                    System.out.println("errror:" + response.body().string());
                }

            }
        });
    }

    static int n = 0;

    static class MyInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            n = n + 1;
            System.out.println(n);
            System.out.println(response);
            return response;
        }
    }


    public static void main(String[] args) {
        System.out.println("start");
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new MyInterceptor()).build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(20);

        n = n +1 ;
        System.out.println(n);
        StopWatch stopWatch = StopWatch.createStarted();
        try {
            JSONObject jsonObject = new ThreadPoolTest().aa(okHttpClient);

            JSONArray array = jsonObject.getJSONArray("transactions");
            System.out.println(array.size());
            CyclicBarrier cyclicBarrier = new CyclicBarrier(array.size());

            array.parallelStream().forEach(obj -> {
                JSONObject json = (JSONObject) obj;
                String txID = json.getString("txID");

                JSONObject param = new JSONObject();
                param.put("value", txID);

                try {
                    new ThreadPoolTest().bb(okHttpClient, param, array.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            stopWatch.stop();
            System.out.println(stopWatch.getTime());
            System.out.println("xxx:" + n);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
