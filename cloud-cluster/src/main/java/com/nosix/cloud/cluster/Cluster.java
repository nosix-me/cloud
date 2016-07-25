package com.nosix.cloud.cluster;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.registry.Registry;
import com.nosix.cloud.rpc.Invoker;
import com.nosix.cloud.rpc.Protocol;

import java.util.List;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi
public interface Cluster<T> extends Invoker<T> {
    void setInterface(Class<T> interfaceClass);

    void setUrl(URL url);

    void setRegistry(Registry registry);

    void setProtocol(Protocol protocol);

    void setLoadBalance(LoadBalance<T> loadBalance);

    void setHaStrategy(HaStrategy<T> haStrategy);

    void refresh(List<URL> urlList);
}
