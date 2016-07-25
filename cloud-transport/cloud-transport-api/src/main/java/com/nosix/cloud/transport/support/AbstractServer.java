package com.nosix.cloud.transport.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.transport.Server;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractServer implements Server {

    protected URL url;

    protected AbstractServerConfiguration configuration;

    protected Object ref;

    public AbstractServer(Object ref, URL url, AbstractServerConfiguration configuration) {
        this.ref = ref;
        this.url = url;
        this.configuration = configuration;
    }

    public URL getUrl() {
        return this.url;
    }
}
