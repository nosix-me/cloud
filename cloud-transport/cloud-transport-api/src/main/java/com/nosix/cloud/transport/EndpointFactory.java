package com.nosix.cloud.transport;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.transport.support.AbstractClientConfiguration;
import com.nosix.cloud.transport.support.AbstractServerConfiguration;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi(scope = SpiScope.SINGLETON)
public interface EndpointFactory {

	public Server createServer(Object ref, URL url, AbstractServerConfiguration configuration);
	
	public Client createClient(URL url, AbstractClientConfiguration configuration);
	
	public void destroyServer(URL url);
	
	public void destoryClient(URL url);
	
}
