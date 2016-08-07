package com.nosix.cloud.common;

import java.util.concurrent.atomic.AtomicLong;

public class RequestIdGenerator {

    private static AtomicLong offset = new AtomicLong(0);

    /**
     * 获取 requestId
     * 
     * @return
     */
    public static long getRequestId() {
        return System.currentTimeMillis() * 100000 + offset.incrementAndGet();
    }

    public static long getRequestIdFromClient() {
        return 0;
    }

}