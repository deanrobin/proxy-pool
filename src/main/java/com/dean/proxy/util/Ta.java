package com.dean.proxy.util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.RandomUtils;

public class Ta implements Taa, Tbb{
    @Override
    public String test(int a) {
        return null;
    }


    private ExecutorService transferPool = new ThreadPoolExecutor(5, 5, 30,
        TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    class X implements Callable {

        String name;
        CyclicBarrier cyclicBarrier;

        @Override
        public Object call() throws Exception {
            int i = RandomUtils.nextInt(100, 5000);
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name);
            try {
                cyclicBarrier.await(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                //e.printStackTrace();
            }
            return name;
        }

        public X(String name, CyclicBarrier cyclicBarrier) {
            this.name = name;
            this.cyclicBarrier = cyclicBarrier;
        }
    }

    //public void x(String[] args) {
    //    CyclicBarrier cb = new CyclicBarrier(2);
    //    System.out.println("begin");
    //
    //    new FutureTask<>()new X("abc", cb));
    //    transferPool.execute(new X("111", cb));
    //    transferPool.execute(new X("kjli", cb));
    //    transferPool.execute(new X("zxc", cb));
    //
    //    try {
    //        cb.await();
    //    } catch (InterruptedException e) {
    //        e.printStackTrace();
    //    } catch (BrokenBarrierException e) {
    //        e.printStackTrace();
    //    }
    //    System.out.println("dean");
    //    transferPool.shutdown();
    //}

    //public static void main(String[] args) {
    //
    //    new Ta().x(args);
    //}
}
