package com.nosix.cloud.rpc;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.transport.support.AbstractClientConfiguration;
import com.nosix.cloud.transport.support.AbstractServerConfiguration;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi
public interface Protocol {

    <T> Service<T> service(Class<T> clz, T obj, URL url);

    <T> Reference<T> reference(Class<T> clz, URL url);

    void destory();
    
    void setClientConfiguration(AbstractClientConfiguration configuration);
    void setServerConfiguration(AbstractServerConfiguration configuration);
}
