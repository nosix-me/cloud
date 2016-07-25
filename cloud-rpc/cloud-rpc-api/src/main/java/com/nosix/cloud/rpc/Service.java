package com.nosix.cloud.rpc;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Service<T> extends Invoker<T> {
    T getProxy();
}
