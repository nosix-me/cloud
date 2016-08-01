package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.rpc.Invoker;

public abstract class AbstractInvoker<T> implements Invoker<T> {

	protected URL url;

	protected volatile boolean init = false;

	public AbstractInvoker(URL url) {
		super();
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



	protected abstract boolean doInit();
	
}
