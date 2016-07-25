package com.nosix.cloud.cluster.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.registry.SubscribeListener;

import java.util.List;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class DefaultClusterSubscribeListener<T> implements SubscribeListener {

    private DefaultCluster<T> cluster;

    public DefaultClusterSubscribeListener(DefaultCluster<T> cluster) {
        this.cluster = cluster;
    }

    public void notify(List<URL> urlList) {
        cluster.refresh(urlList);
    }
}
