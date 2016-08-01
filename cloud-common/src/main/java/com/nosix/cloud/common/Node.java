package com.nosix.cloud.common;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Node {

    void init();

    URL getUrl();

    boolean isAvaialble();

    void destory();
}
