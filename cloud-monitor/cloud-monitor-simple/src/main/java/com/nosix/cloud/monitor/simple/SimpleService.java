package com.nosix.cloud.monitor.simple;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface SimpleService {

    String STATISTICS_DIRECTORY = "/opt/monitor/statistic";

    String CHARS_DIRECTORY = "/opt/monitor/chart";

    String INTERFACE = "interface";

    String METHOD = "method";

    String GROUP = "group";

    String VERSION = "version";

    String SUCCESS = "success";

    String FAILURE = "failure";

    String CONCURRENT = "concurrent";

    String PROVIDER = "provider";

    String[] TYPES = { SUCCESS, FAILURE , CONCURRENT};
}
