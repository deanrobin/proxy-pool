package com.dean.proxy.service.operations;

import java.util.ArrayList;
import java.util.List;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.cache.ProxyCachePool;
import com.dean.proxy.db.ProxyMapper;
import com.dean.proxy.response.ProxyRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dean
 */
@Service
public class ProxyService {
    @Autowired
    ProxyMapper proxyMapper;
    @Autowired
    ProxyCachePool proxyCachePool;

    final static String SORT_KEY = "id";
    final static Integer COUNT = 20;

    public Integer usableCount() {
        return proxyMapper.successCount();
    }

    public Integer count() {
        return proxyMapper.count();
    }

    public Integer unverifiedCount() {
        return proxyMapper.unverifiedCount();
    }

    public List<ProxyRes> getList(String type, String source, String country) {
        List<Proxy> list = proxyMapper.query(country, source, type, SORT_KEY, COUNT);
        List<ProxyRes> prList = new ArrayList<>(list.size());

        for (Proxy p : list) {
            Integer number = proxyCachePool.selectByInternalId(p.getInternalId());
            ProxyRes res = ProxyRes.transform(p, number);
            prList.add(res);
        }

        return prList;
    }

    public List<ProxyRes> getLast(String type, String country, Integer count) {
        List<Proxy> list = proxyMapper.query(country, null, type, "validate_time", count);
        List<ProxyRes> prList = new ArrayList<>(list.size());

        for (Proxy p : list) {
            Integer number = proxyCachePool.selectByInternalId(p.getInternalId());
            ProxyRes res = ProxyRes.transform(p, number);
            prList.add(res);
        }

        return prList;
    }
}
