package com.dean.proxy.response;

import com.dean.proxy.bean.Proxy;

public class ProxyRes {

    private String internalId;
    private String ip;
    private Integer port;
    private Long validateTime;
    private String type;
    private String source;
    private Integer used;

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Long getValidateTime() {
        return validateTime;
    }

    public void setValidateTime(Long validateTime) {
        this.validateTime = validateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }


    public static ProxyRes transform(Proxy proxy, Integer used) {
        ProxyRes proxyRes = new ProxyRes();
        proxyRes.setInternalId(proxy.getInternalId());
        proxyRes.setIp(proxy.getIp());
        proxyRes.setPort(proxy.getPort());
        proxyRes.setSource(proxy.getSource());
        proxyRes.setType(proxy.getType());
        proxyRes.setValidateTime(proxy.getValidateTime());
        proxyRes.setUsed(used);
        return proxyRes;
    }

    public static ProxyRes transform(Proxy proxy) {
        return transform(proxy, 0);
    }
}
