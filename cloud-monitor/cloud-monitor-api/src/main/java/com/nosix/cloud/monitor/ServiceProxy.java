package com.nosix.cloud.monitor;

import com.nosix.cloud.common.URL;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface ServiceProxy {

    Object wrapper(Object service, URL url);
}
