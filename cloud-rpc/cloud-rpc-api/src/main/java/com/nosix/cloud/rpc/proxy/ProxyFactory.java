package com.nosix.cloud.rpc.proxy;

import com.nosix.cloud.common.extension.Spi;

import java.lang.reflect.InvocationHandler;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi
public interface ProxyFactory {
    <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler);
}
