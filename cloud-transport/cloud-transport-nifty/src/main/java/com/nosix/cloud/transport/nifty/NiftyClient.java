package com.nosix.cloud.transport.nifty;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.reflect.ReflectFactory;
import com.nosix.cloud.common.util.ExceptionUtil;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;
import com.nosix.cloud.transport.support.AbstractClient;
import com.nosix.cloud.transport.support.AbstractClientConfiguration;
import com.nosix.cloud.transport.support.DefaultResponse;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class NiftyClient extends AbstractClient {


    private TServiceClient tServiceClient;

    private TTransport transport;

    public NiftyClient(URL url, AbstractClientConfiguration abstractClientConfiguration) {
        super(url, abstractClientConfiguration);
        init();
    }

    private void init() {
       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            @SuppressWarnings("unchecked")
			Class<TServiceClientFactory<TServiceClient>> fi = (Class<TServiceClientFactory<TServiceClient>>) classLoader.loadClass(url.getPath().replace("$Iface", "") + "$Client$Factory");
            TServiceClientFactory<TServiceClient> clientFactory = fi.newInstance();
            TSocket tsocket = new TSocket(url.getHost(), url.getPort());
            tsocket.setTimeout(url.getIntParameter(URLParam.timeout.getName(),Integer.parseInt(URLParam.timeout.getValue())));
            transport = new TFramedTransport(tsocket);
            TProtocol protocol = new TBinaryProtocol(transport);
            tServiceClient = clientFactory.getClient(protocol);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public Response write(Request request) {
        if(!this.isAvailable()) {
            return genrateExceptionResult("transport is not open");
        }
        try {
        	Method method = ReflectFactory.name2Method(request.getInterfaceName(), request.getMethodName());
            Object result = method.invoke(tServiceClient, request.getParameters());
            Response response = new DefaultResponse();
            response.setValue(result);
            response.setException(false);
            return response;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return genrateExceptionResult(ExceptionUtil.outException(e));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return genrateExceptionResult(ExceptionUtil.outException(e));
		} 
    }

    private Response genrateExceptionResult(String message) {
        Response response = new DefaultResponse();
        response.setException(true);
        response.setValue(message);
        return response;
    }
    public boolean open() {
        if(tServiceClient == null) {
            return false;
        }
        if(transport == null) {
            return  false;
        }
        try {
            transport.open();
            return  true;
        } catch (TTransportException e) {
            e.printStackTrace();
            return false;

        }
    }

    public void close() {
        if(transport != null) {
            transport.close();
        }
    }

    public boolean isAvailable() {
        if(tServiceClient == null) {
            return false;
        }

        if(transport == null) {
            return false;
        }

        if(transport.isOpen()) {
            return true;
        } else {
            return false;
        }
    }
}
