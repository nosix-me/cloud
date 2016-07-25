package com.nosix.cloud.config;

import com.nosix.cloud.registry.support.AbstractRegistryConfig;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class RegistryConfig extends AbstractConfig {

    private String protocol;

    private String address;

    private AbstractRegistryConfig extConfig;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AbstractRegistryConfig getExtConfig() {
        return extConfig;
    }

    public void setExtConfig(AbstractRegistryConfig extConfig) {
        this.extConfig = extConfig;
    }
}
