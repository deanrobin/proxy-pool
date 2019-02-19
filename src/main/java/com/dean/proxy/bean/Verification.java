package com.dean.proxy.bean;

/**
 * @author dean
 */
public class Verification {
    private int id;
    private String internalId;
    private Boolean use;
    private Long verifyAvailableTime;
    private Long unavailableTime;
    private Long duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }

    public Long getVerifyAvailableTime() {
        return verifyAvailableTime;
    }

    public void setVerifyAvailableTime(Long verifyAvailableTime) {
        this.verifyAvailableTime = verifyAvailableTime;
    }

    public Long getUnavailableTime() {
        return unavailableTime;
    }

    public void setUnavailableTime(Long unavailableTime) {
        this.unavailableTime = unavailableTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    /**
     * 此方法只用于insert时候构建
     */
    public Verification(String internalId, boolean use, Long verifyAvailableTime) {
        this.internalId = internalId;
        this.use = use;
        this.verifyAvailableTime = verifyAvailableTime;

        if (!use) {
            this.unavailableTime = verifyAvailableTime;
            //this.duration = 0L;
        }
    }

    public Verification(int id, String internalId, Boolean use, Long verifyAvailableTime, Long unavailableTime,
                        Long duration) {
        this.id = id;
        this.internalId = internalId;
        this.use = use;
        this.verifyAvailableTime = verifyAvailableTime;
        this.unavailableTime = unavailableTime;
        this.duration = duration;
    }

    //public Verification(String internalId, Long verifyAvailableTime) {
    //    this.internalId = internalId;
    //    this.use = true;
    //    this.verifyAvailableTime = verifyAvailableTime;
    //}
}
