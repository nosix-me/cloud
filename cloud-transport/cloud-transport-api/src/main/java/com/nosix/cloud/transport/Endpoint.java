package com.nosix.cloud.transport;

import com.nosix.cloud.common.URL;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Endpoint {
    boolean open();

    void close();

    boolean isAvailable();

    URL getUrl();
}
