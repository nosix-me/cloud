package com.nosix.cloud.registry;


import com.nosix.cloud.common.URL;

import java.util.List;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Registry {

    void registry(URL url);

    void unRegistry(URL url);

    void subscribe(URL url, SubscribeListener listener);

    void unSubscribte(URL url, SubscribeListener listener);

    List<URL> discover(URL url);

}
