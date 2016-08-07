package com.nosix.cloud.rpc;

import com.nosix.cloud.common.URL;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Node {

    void init();

    void destroy();

    boolean isAvailable();

    URL getURL();
}
