package com.nosix.cloud.transport;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Client extends Endpoint {
    Response write(Request request);
}
