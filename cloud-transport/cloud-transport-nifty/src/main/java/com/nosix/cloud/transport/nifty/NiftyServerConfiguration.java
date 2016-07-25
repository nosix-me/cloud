package com.nosix.cloud.transport.nifty;

import com.nosix.cloud.transport.support.AbstractServerConfiguration;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class NiftyServerConfiguration extends AbstractServerConfiguration {

    private Integer bossCount = 1;

    private Integer workerCount = Runtime.getRuntime().availableProcessors();

    public Integer getBossCount() {
        return this.bossCount;
    }
    public void setBossCount(Integer bossCount) {
        this.bossCount = bossCount;
    }
    public Integer getWorkerCount() {
        return this.workerCount;
    }
    public void setWorkerCount(Integer workerCount) {
        this.workerCount = workerCount;
    }
}
