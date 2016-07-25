package com.nosix.cloud.rpc.support;

import java.lang.reflect.Method;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.util.ExceptionUtil;
import com.nosix.cloud.rpc.Service;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;
import com.nosix.cloud.transport.support.DefaultResponse;

public abstract class AbstractService<T> extends AbstractInvoker<T> implements Service<T> {

	protected T proxy;
	
	public AbstractService(Class<T> clz, T proxy ,URL url) {
		super(clz, url);
		this.proxy = proxy;
	}

	public T getProxy() {
		return proxy;
	}

	public Response invoke(Request request) {
		Response response = new DefaultResponse();
		if(request == null) {
			response.setException(true);
			response.setValue("request is null");
			return response;
		}
		try{
			ClassLoader classlodar = Thread.currentThread().getContextClassLoader();
	    	Class<?> clazz = classlodar.loadClass(request.getInterfaceName()+"$Iface");
	    	Class<?> [] types = null;
	    	if(request.getParameters() != null && request.getParameters().length > 0) {
	    		types = new Class<?>[request.getParameters().length];
	    		for(int i = 0; i < request.getParameters().length; i++) {
	    			types[i] = request.getParameters()[i].getClass();
	    		}
	    	}
	    	Method method = null;
	    	if(types == null) {
	    		method = clazz.getMethod(request.getMethodName());
	    	} else {
	    		method = clazz.getMethod(request.getMethodName(),types);
	    	}
	    	if(method == null) {
	    		response.setException(true);
	    		response.setValue("no such method:"+request.getMethodName());
	    		return response;
	    	}
	    	Object value = method.invoke(proxy, request.getParameters());
	    	response.setException(false);
	    	response.setValue(value);
	    	return response;
		}catch(Exception e) {
			e.printStackTrace();
			response.setException(true);
			response.setValue(ExceptionUtil.outException(e));
			return response;
		} 
	}
}
