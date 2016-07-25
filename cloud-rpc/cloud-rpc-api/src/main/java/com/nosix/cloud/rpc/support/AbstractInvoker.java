package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.rpc.Invoker;

public abstract class AbstractInvoker<T> implements Invoker<T> {
	
	protected Class<T> clz;
	
	protected URL url;

	protected volatile boolean init = false;
	public AbstractInvoker(Class<T> clz, URL url) {
		super();
		this.clz = clz;
		this.url = url;
	}

	public void init() {
		if(init) {
			return;
		}
		boolean result = doInit();
		if(result) {
			init = true;
		} else {
			init = false;
		}
	}
	
	
	public URL getURL() {
		return this.url;
	}

	public Class<T> getInterface() {
		return clz;
	}

	protected abstract boolean doInit();
	
}
