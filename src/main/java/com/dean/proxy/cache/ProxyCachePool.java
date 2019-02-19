package com.dean.proxy.cache;

import java.util.Random;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.db.CounterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 真正的池
 * @author dean
 */
@Component
public class ProxyCachePool {
    @Autowired
    CounterMapper counterMapper;

    /**
     * 发现可用的代理 放入池子中
     * @param proxy
     */
    public void add(Proxy proxy) {
        AvailableCache.put(proxy.getInternalId(), proxy);
        Counter.put(proxy.getInternalId());
    }

    /**
     * 获取一个可用的代理
     * @return proxy
     */
    public Proxy randomGetProxy() {
        Random random = new Random();
        Object[] values = AvailableCache.getValuesArray();
        if (values.length == 0) {
            return null;
        }
        Proxy proxy = (Proxy) values[random.nextInt(values.length)];

        Counter.andOne(proxy.getInternalId());
        return proxy;
    }

    /**
     * 获取一个可用的代理
     * @return String ip:port
     */
    public String randomGetIp() {
        Proxy proxy = randomGetProxy();
        return proxy.getIp() + ":" + proxy.getPort();
    }

    /**
     * 移除不可用的 包含持久化操作
     * @param internalId
     * @return
     */
    public boolean remove(String internalId) {
        AvailableCache.remove(internalId);
        Integer count = Counter.get(internalId);
        Counter.remove(internalId);

        com.dean.proxy.bean.Counter counter =
            new com.dean.proxy.bean.Counter(count, internalId);
        counterMapper.insert(counter);
        return true;
    }

    /**
     * 获取某个代理使用次数
     * @param internalId
     * @return
     */
    public Integer selectByInternalId(String internalId) {
        Integer num = Counter.get(internalId);
        return num == null ? 0 : num;
    }

    public Integer size() {
        return AvailableCache.size();
    }
}
