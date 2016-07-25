package com.nosix.cloud.config;

import com.nosix.cloud.transport.support.AbstractClientConfiguration;
import com.nosix.cloud.transport.support.AbstractServerConfiguration;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ProtocolConfig  extends AbstractConfig{

    private String protocol;

    private String transport;


    //------server------//
    private String host;

    private Integer port;
    
    private AbstractServerConfiguration serverConfig;

    //------client------//
    private String proxy;

    private String cluster;

    private String loadbalance;

    private String haStrategy;
    
    private AbstractClientConfiguration clientConfig;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer  getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public String getHaStrategy() {
        return haStrategy;
    }

    public void setHaStrategy(String haStrategy) {
        this.haStrategy = haStrategy;
    }

	public AbstractServerConfiguration getServerConfig() {
		return serverConfig;
	}

	public void setServerConfig(AbstractServerConfiguration serverConfig) {
		this.serverConfig = serverConfig;
	}

	public AbstractClientConfiguration getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(AbstractClientConfiguration clientConfig) {
		this.clientConfig = clientConfig;
	}
	
}
