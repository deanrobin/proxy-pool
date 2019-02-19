package com.dean.proxy;

import java.util.Map;

import com.dean.proxy.service.ProxyService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author dean
 */
@Service
public class ProxyPoolContext implements ApplicationContextAware, InitializingBean {

    Map<String, ProxyService> serviceMap;

    ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        serviceMap = applicationContext.getBeansOfType(ProxyService.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ProxyService getInstance(String type) {
        for (Map.Entry<String, ProxyService> entry : serviceMap.entrySet()) {
            if (entry.getValue().getProxyId().equals(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Map<String, ProxyService> getServiceMap() {
        return serviceMap;
    }
}
