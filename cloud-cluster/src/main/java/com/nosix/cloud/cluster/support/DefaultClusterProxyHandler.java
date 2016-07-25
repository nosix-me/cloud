package com.nosix.cloud.cluster.support;

import com.nosix.cloud.cluster.Cluster;
import com.nosix.cloud.common.reflect.ReflectFactory;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;
import com.nosix.cloud.transport.support.DefaultRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class DefaultClusterProxyHandler<T> implements InvocationHandler {

    private Class<T> clz;

    private Cluster<T> cluster;

    public DefaultClusterProxyHandler(Class<T> clz, Cluster<T> cluster) {
        this.clz = clz;
        this.cluster = cluster;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String group = cluster.getURL().getGroup();
        String version = cluster.getURL().getVersion();
        String interfaceName = ReflectFactory.class2Name(clz);
        String methdName = ReflectFactory.method2Name(method);
        Request request = new DefaultRequest(group, version, interfaceName, methdName);
        request.setParameters(args);
        request.setRequestId(0l);
        try {
            Response response = cluster.invoke(request);
            return response.getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
