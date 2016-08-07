package com.nosix.cloud.monitor.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.monitor.Monitor;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi(name = "simple")
public class SimpleMonitorFactory extends AbstractMonitorFactory {

    @Override
    protected Monitor createMonitor(URL url) {
        return new SimpleMonitor();
    }
}
