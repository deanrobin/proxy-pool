package com.dean.proxy.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.dean.proxy.bean.Proxy;

/**
 * 计数器
 * @author dean
 */
public class Counter {

    private static ConcurrentHashMap<String, Integer> counter = new ConcurrentHashMap();

    public static void put(String internalId) {
        if (counter.contains(internalId)) {
            return;
        }
        counter.put(internalId, 0);
    }

    public static Integer get(String internalId) {
        if (!counter.contains(internalId)) {
            counter.put(internalId, 0);
            return 0;
        }
        return counter.get(internalId);
    }

    public static void remove(String internalId) {
        counter.remove(internalId);
    }

    public static void andOne(String internalId) {
        Integer number = counter.get(internalId);
        number = number + 1;
        counter.put(internalId, number);
    }
}
