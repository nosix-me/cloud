package com.nosix.cloud.monitor.simple;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class Statistic {
    private String serviceName;
    private String serviceVersion;
    private String serviceGroup;
    private String method;
    private int concurrent;
    private long takeTime;
    private boolean error;

    public Statistic(String serviceName, String serviceVersion, String serviceGroup, String method, int concurrent, long takeTime, boolean error) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.method = method;
        this.concurrent = concurrent;
        this.takeTime = takeTime;
        this.error = error;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(int concurrent) {
        this.concurrent = concurrent;
    }

    public long getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(long takeTime) {
        this.takeTime = takeTime;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
