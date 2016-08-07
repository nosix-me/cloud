package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

public abstract class AbstractReference<T> extends AbstractInvoker<T> implements Reference<T> {

	protected Class<T> clz;

	public AbstractReference(Class<T> clz, URL url) {
		super(url);
	}

	public Response invoke(Request request) {
		return doInvoke(request);
	}

	public Class<T> getInterface() {
		return clz;
	}

	protected abstract Response doInvoke(Request request);
}
