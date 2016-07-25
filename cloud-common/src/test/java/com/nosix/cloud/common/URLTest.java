package com.nosix.cloud.common;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class URLTest {

    @Test
    public void UrlTest() {
        URL url = URL.valueOf("zookeeper://127.0.0.1:8080/com.nosix.service.HelloService?version=1.0.0&group=test");
        assertEquals("zookeeper", url.getProtocol());
        assertEquals("127.0.0.1", url.getHost());
        assertEquals(8080, url.getPort());
        assertEquals("com.nosix.service.HelloService", url.getPath());
    }
}
