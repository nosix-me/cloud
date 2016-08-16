package com.nosix.cloud.transport.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.transport.Client;
import com.nosix.cloud.transport.EndpointFactory;
import com.nosix.cloud.transport.Server;

import java.util.HashMap;
import java.util.Map;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractEndpointFactory implements EndpointFactory{
    private Client client;
    private Server server;
    public Server createServer(Object ref, URL url, AbstractServerConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("createServer error : server config is null");
        }

        server = doCreateServer(ref, url, configuration);
        return server;
    }

    public Client createClient(URL url, AbstractClientConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("createClient error : client config is null");
        }
        client = doCreateClient(url, configuration);
        return client;
    }

    public void destroyServer(URL url) {
        server.close();
    }

    public void destoryClient(URL url) {
        client.close();
    }

    protected abstract Server doCreateServer(Object ref, URL url, AbstractServerConfiguration configuration);

    protected abstract Client doCreateClient(URL url, AbstractClientConfiguration configuration);

}
