package com.nosix.cloud.rpc.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.rpc.Protocol;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.rpc.Service;
import com.nosix.cloud.transport.support.AbstractClientConfiguration;
import com.nosix.cloud.transport.support.AbstractServerConfiguration;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractProtocol implements Protocol {

	private static final Logger logger = LoggerFactory.getLogger(AbstractProtocol.class);
	protected AbstractClientConfiguration clientConfiguration;
	protected AbstractServerConfiguration serverConfiguration;

	public void setClientConfiguration(
			AbstractClientConfiguration clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}
	
	public void setServerConfiguration(
			AbstractServerConfiguration serverConfiguration) {
		this.serverConfiguration = serverConfiguration;
	}

	public <T> Service<T> service(Class<T> clz, T obj, URL url) {
		if(url == null) {
			logger.error("service error: url is null");
			return null;
		}
		@SuppressWarnings("unchecked")
		Service<T> service = (Service<T>) ProtocolFactory.getService(url);
		if(service != null) {
			logger.warn("sevice warning: url = {} already exists", url);
			return service;
		}
		service = doService(clz, obj, url);
		service.init();
		ProtocolFactory.addService(service);
        return service;
    }

    public <T> Reference<T> reference(Class<T> clz, URL url) {
    	if(url == null) {
    		logger.error("reference error: url is null");
    		return null;
    	}
    	@SuppressWarnings({"unchecked"})
		Reference<T> reference = (Reference<T>) ProtocolFactory.getReference(url);
    	if(reference != null) {
    		logger.warn("client warning: reference={} already exists", url);
    		return reference;
    	}
    	reference = doReference(clz, url);
    	reference.init();
    	ProtocolFactory.addReference(reference);
        return reference;
    }

    public void destory() {
    	ProtocolFactory.destroyAllReference();
    	ProtocolFactory.destroyAllService();
    }
    
    protected abstract <T> Service<T> doService(Class<T> clz,T obj, URL url);
    protected abstract <T> Reference<T> doReference(Class<T> clz, URL url);
}
