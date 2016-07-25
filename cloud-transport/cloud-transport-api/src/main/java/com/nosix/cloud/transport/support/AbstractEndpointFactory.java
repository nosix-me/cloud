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
    protected Map<String, Server> serverMap = new HashMap<String, Server>();
    protected Map<String, Client> clientMap = new HashMap<String, Client>();

    public Server createServer(Object ref, URL url, AbstractServerConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("createServer error : server config is null");
        }

        synchronized (serverMap) {
            String key = url.getHostAndPort();
            Server server = serverMap.get(key);
            if (server != null) {
                return server;
            }

            server = doCreateServer(ref, url, configuration);
            serverMap.put(key, server);
            return server;
        }
    }

    public Client createClient(URL url, AbstractClientConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("createClient error : client config is null");
        }

        synchronized (clientMap) {
            String key = url.getHostAndPort();
            Client client = clientMap.get(key);
            if (client != null) {
                return client;
            }

            client = doCreateClient(url, configuration);
            clientMap.put(key, client);
            return client;
        }
    }

    public void destroyServer(URL url) {
        synchronized (serverMap) {
            String key = url.getHostAndPort();
            Server server = serverMap.get(key);
            if (server != null) {
                server.close();
                serverMap.remove(key);
            }
        }
    }

    public void destoryClient(URL url) {
        synchronized (clientMap) {
            String key = url.getHostAndPort();
            Client client = clientMap.get(key);
            if (client != null) {
                client.close();
                clientMap.remove(key);
            }
        }
    }

    protected abstract Server doCreateServer(Object ref, URL url, AbstractServerConfiguration configuration);

    protected abstract Client doCreateClient(URL url, AbstractClientConfiguration configuration);

}
