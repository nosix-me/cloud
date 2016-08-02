package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.rpc.Invoker;

public abstract class AbstractInvoker<T> extends AbstractNode implements Invoker<T> {

	public AbstractInvoker(URL url) {
		super(url);
	}
}
