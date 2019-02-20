package com.dean.proxy;

import java.util.Map;

import com.dean.proxy.service.IProxyService;
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

    Map<String, IProxyService> serviceMap;

    ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        serviceMap = applicationContext.getBeansOfType(IProxyService.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public IProxyService getInstance(String type) {
        for (Map.Entry<String, IProxyService> entry : serviceMap.entrySet()) {
            if (entry.getValue().getProxyId().equals(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Map<String, IProxyService> getServiceMap() {
        return serviceMap;
    }
}
