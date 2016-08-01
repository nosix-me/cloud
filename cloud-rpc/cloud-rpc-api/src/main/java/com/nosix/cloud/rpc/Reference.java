package com.nosix.cloud.rpc;

import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Reference<T> extends Invoker<T> {

	Integer getActiveCount();

	Response invoke(Request request);

	Class<T> getInterface();
}
