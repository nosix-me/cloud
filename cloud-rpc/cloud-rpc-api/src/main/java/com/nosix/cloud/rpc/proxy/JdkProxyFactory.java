package com.nosix.cloud.rpc.proxy;

import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@Spi(scope = SpiScope.PROTOTYPE, name = "jdk")
public class JdkProxyFactory implements ProxyFactory {

	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler) {
		return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clz}, invocationHandler);
	}
}