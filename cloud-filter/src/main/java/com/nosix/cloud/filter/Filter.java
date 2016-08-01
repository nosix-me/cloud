package com.nosix.cloud.filter;


import com.nosix.cloud.rpc.Invoker;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Filter {

    Response filter(Invoker<?> invoker, Request request);
}
