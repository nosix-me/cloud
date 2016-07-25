package com.nosix.cloud.rpc.support;

import java.util.concurrent.atomic.AtomicInteger;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.extension.SpiLoader;
import com.nosix.cloud.transport.Client;
import com.nosix.cloud.transport.EndpointFactory;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;
import com.nosix.cloud.transport.support.AbstractClientConfiguration;

public class DefaultRefernce<T> extends AbstractReference<T> {
	protected AtomicInteger activeRefererCount = new AtomicInteger(0);
	
	private EndpointFactory endpointFactory;
	
	private Client client;
	
	public DefaultRefernce(Class<T> clz, URL url, AbstractClientConfiguration configuration) {
		super(clz, url);
		endpointFactory = SpiLoader.getInstance(EndpointFactory.class).getExtension(url.getParameter(URLParam.transport.getName()));
		client = endpointFactory.createClient(url, configuration);
	}

	public void destroy() {
		incrActiveCount();
		try{
			endpointFactory.destoryClient(url);
		}catch(Exception e) {
            decrActiveCount();
		}
	}

	public boolean isAvailable() {
		return client.isAvailable();
	}

	@Override
	protected Response doInvoke(Request request) {
		return client.write(request);
	}

	@Override
	protected boolean doInit() {
		return client.open();
	}
	
	protected void incrActiveCount() {
	   activeRefererCount.incrementAndGet();
	}
	protected void decrActiveCount() {
	    activeRefererCount.decrementAndGet();
	}

	@Override
	public Integer getActiveCount() {
		return activeRefererCount.intValue();
	}
}
