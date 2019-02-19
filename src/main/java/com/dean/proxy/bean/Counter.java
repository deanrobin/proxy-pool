package com.dean.proxy.bean;

public class Counter {

    private Integer id;
    private Integer count;
    private String internalId;
    private Long timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Counter(Integer id, Integer count, String internalId, Long timestamp) {
        this.id = id;
        this.count = count;
        this.internalId = internalId;
        this.timestamp = timestamp;
    }

    public Counter(Integer count, String internalId) {
        this.count = count;
        this.internalId = internalId;
        this.timestamp = System.currentTimeMillis();
    }
}
