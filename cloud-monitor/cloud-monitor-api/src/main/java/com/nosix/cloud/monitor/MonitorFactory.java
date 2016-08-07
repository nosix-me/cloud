package com.nosix.cloud.monitor;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi
public interface MonitorFactory<T> {

    Monitor getMonitor(URL url);
}