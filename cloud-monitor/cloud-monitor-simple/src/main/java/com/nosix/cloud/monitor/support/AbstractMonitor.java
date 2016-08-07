package com.nosix.cloud.monitor.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.monitor.Monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractMonitor implements Monitor {

    private Map<String, AtomicInteger> concurrents = new HashMap<String, AtomicInteger>();

    @Override
    public AtomicInteger getConcurrent(URL url) {
        String key = url.getServiceUri();
        AtomicInteger concurrent = concurrents.get(key);
        if (concurrent == null) {
            concurrents.putIfAbsent(key, new AtomicInteger());
            concurrent = concurrents.get(key);
        }
        return concurrent;
    }
}
