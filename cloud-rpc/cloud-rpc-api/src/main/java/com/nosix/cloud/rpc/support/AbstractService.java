package com.nosix.cloud.rpc.support;

import java.lang.reflect.Method;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.reflect.ReflectFactory;
import com.nosix.cloud.common.util.ExceptionUtil;
import com.nosix.cloud.rpc.Service;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;
import com.nosix.cloud.transport.support.DefaultResponse;

public abstract class AbstractService<T> extends AbstractInvoker<T> implements Service<T> {

	public AbstractService(Class<T> clz, URL url) {
		super(clz, url);
	}
}
