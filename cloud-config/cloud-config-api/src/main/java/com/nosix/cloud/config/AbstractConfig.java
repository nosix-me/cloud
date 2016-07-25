package com.nosix.cloud.config;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractConfig {
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
