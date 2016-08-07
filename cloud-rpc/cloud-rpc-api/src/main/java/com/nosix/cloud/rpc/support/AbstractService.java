package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.rpc.Service;

public abstract class AbstractService<T> extends AbstractNode implements Service<T> {

	public AbstractService(URL url) {
		super(url);
	}
}
