package com.dean.proxy.bean;

/**
 * @author dean
 */
public class Proxy {

    private Integer id;
    private String internalId;

    private String ip;
    private Integer port;
    // 匿名等级
    private Integer anonymity;
    // http https
    private String type;
    // 电信 or联通
    private String source;
    // 国家
    private String country;
    // 地址
    private String location;
    // 响应时间
    private Long responseTime;
    // 录入时间
    private Long timestamp;

    private Integer validateCount;
    private Integer status;
    private Integer availableCount;
    private Long validateTime;
    private Integer continuousUnavailableNumber;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(Integer anonymity) {
        this.anonymity = anonymity;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getValidateCount() {
        return validateCount;
    }

    public void setValidateCount(Integer validateCount) {
        this.validateCount = validateCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Integer availableCount) {
        this.availableCount = availableCount;
    }

    public Long getValidateTime() {
        return validateTime;
    }

    public void setValidateTime(Long validateTime) {
        this.validateTime = validateTime;
    }

    public Integer getContinuousUnavailableNumber() {
        return continuousUnavailableNumber;
    }

    public void setContinuousUnavailableNumber(Integer continuousUnavailableNumber) {
        this.continuousUnavailableNumber = continuousUnavailableNumber;
    }

    public Proxy(String ip, Integer port, Integer anonymity, String type, String source, String country,
                 String location, Long responseTime, String internalId) {
        this.ip = ip;
        this.port = port;
        this.anonymity = anonymity;
        this.type = type;
        this.source = source;
        this.country = country;
        this.location = location;
        this.responseTime = responseTime;
        this.internalId = internalId;
        this.timestamp = System.currentTimeMillis();
    }

    public Proxy() {

    }
}
