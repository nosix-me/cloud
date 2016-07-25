package com.nosix.cloud.rpc;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Reference<T> extends Invoker<T> {
	Integer getActiveCount();
}
