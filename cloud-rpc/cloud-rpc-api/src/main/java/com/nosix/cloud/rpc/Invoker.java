package com.nosix.cloud.rpc;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Invoker<T> {
    void init();

    void destroy();

    boolean isAvailable();

    URL getURL();
}
