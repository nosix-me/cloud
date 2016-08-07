package com.nosix.cloud.monitor.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.monitor.ServiceProxy;

import java.lang.reflect.Proxy;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class DefaultServiceProxy implements ServiceProxy {
    @Override
    public Object wrapper(Object service, URL url) {
        return Proxy.newProxyInstance(service.getClass().getClassLoader(),service.getClass().getInterfaces(), new ServiceProxyHandler(service, url));
    }
}
