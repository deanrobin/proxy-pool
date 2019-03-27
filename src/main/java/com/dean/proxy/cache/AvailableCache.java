package com.dean.proxy.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.dean.proxy.bean.Proxy;

/**
 * @author dean
 */
public class AvailableCache {

    private static ConcurrentHashMap<String, Proxy> cache = new ConcurrentHashMap();

    //public static void put(String internalId, Proxy proxy) {
    //    cache.put(internalId, proxy);
    //}
    //
    //public static Proxy get(String internalId) {
    //    return cache.get(internalId);
    //}
    //
    //public static void remove(String internalId) {
    //    cache.remove(internalId);
    //}
    //
    //public static Integer size() {
    //    return cache.size();
    //}
    //
    //public static Object[] getValuesArray() {
    //    return cache.values().toArray();
    //}
}
