package com.nosix.cloud.monitor;

import com.nosix.cloud.common.URL;

import java.util.List;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface MonitorService {

    String INTERFACE = "interface";

    String METHOD = "method";

    String GROUP = "group";

    String VERSION = "version";

    String SUCCESS = "success";

    String FAILURE = "failure";


    void collect(URL statistics);

    List<URL> lookup(URL query);
}
