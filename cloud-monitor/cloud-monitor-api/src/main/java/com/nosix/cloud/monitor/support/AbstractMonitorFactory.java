package com.nosix.cloud.monitor.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.monitor.MonitorFactory;
import com.nosix.cloud.monitor.Monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractMonitorFactory<T> implements MonitorFactory<T> {

    private static final ReentrantLock LOCK = new ReentrantLock();

    private static final Map<String, Monitor> monitors = new HashMap<String, Monitor>();

    @Override
    public Monitor getMonitor(URL url) {
        String key = url.getServiceUri();
        LOCK.lock();
        try {
            Monitor monitor = monitors.get(key);
            if(monitor != null) {
                return monitor;
            }
            monitor = createMonitor(url);
            if(monitor == null) {
                throw new IllegalStateException("Can not create monior" + url);
            }
            monitors.put(key, monitor);
            return monitor;
        } finally {
            LOCK.unlock();
        }
    }

    protected abstract Monitor createMonitor(URL url);
}
