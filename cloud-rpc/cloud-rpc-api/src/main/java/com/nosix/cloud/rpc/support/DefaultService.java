package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.extension.ExtentionLoader;
import com.nosix.cloud.transport.EndpointFactory;
import com.nosix.cloud.transport.Server;
import com.nosix.cloud.transport.support.AbstractServerConfiguration;

public class DefaultService<T> extends AbstractService<T> {

	private Server server;
	
	private EndpointFactory endpointFactory;
	
	public DefaultService(T proxy, URL url, AbstractServerConfiguration configuration) {
		super( url);
		endpointFactory = ExtentionLoader.getExtensionLoader(EndpointFactory.class).getExtension(url.getParameter(URLParam.transport.getName()));
		server = endpointFactory.createServer(proxy, url, configuration);
	}

	public void destroy() {
		endpointFactory.destroyServer(url);
	}

	public boolean isAvailable() {
		return server.isAvailable();
	}

	@Override
	protected boolean doInit() {
		return server.open();
	}

}
