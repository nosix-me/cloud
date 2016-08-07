package com.nosix.cloud.monitor;

import com.nosix.cloud.common.URL;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Monitor extends MonitorService {

    void start();

    AtomicInteger getConcurrent(URL url);

    void collect(URL url, String methodName, int concurrent, long takeTime, boolean isError);

    List<URL> lookup(URL query);
}
