package com.nosix.cloud.rpc;


import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi
public interface Filter {

    Response filter(Invoker<?> invoker, Request request);
}
