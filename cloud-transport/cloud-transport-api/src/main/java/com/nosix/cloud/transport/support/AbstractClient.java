package com.nosix.cloud.transport.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.transport.Client;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractClient implements Client {

    protected URL url;

    protected AbstractClientConfiguration configuration;

    public AbstractClient(URL url, AbstractClientConfiguration configuration) {
        this.url = url;
        this.configuration = configuration;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }


}