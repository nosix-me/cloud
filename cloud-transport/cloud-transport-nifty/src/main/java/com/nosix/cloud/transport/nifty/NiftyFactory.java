package com.nosix.cloud.transport.nifty;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.transport.Client;
import com.nosix.cloud.transport.Server;
import com.nosix.cloud.transport.support.AbstractClientConfiguration;
import com.nosix.cloud.transport.support.AbstractEndpointFactory;
import com.nosix.cloud.transport.support.AbstractServerConfiguration;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi(name = "nifty", scope = SpiScope.SINGLETON)
public class NiftyFactory extends AbstractEndpointFactory {

    protected Server doCreateServer(Object ref, URL url, AbstractServerConfiguration configuration) {
        return new NiftyServer(ref, url, configuration);
    }

    protected Client doCreateClient(URL url, AbstractClientConfiguration configuration) {
        return new NiftyClientPool(url, configuration);
    }
}
