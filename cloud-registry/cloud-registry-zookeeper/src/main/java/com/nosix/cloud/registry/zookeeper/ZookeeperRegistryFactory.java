package com.nosix.cloud.registry.zookeeper;

import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.retry.RetryNTimes;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.registry.Registry;
import com.nosix.cloud.registry.support.AbstractRegistryConfig;
import com.nosix.cloud.registry.support.AbstractRegistryFactory;

@Spi(name="zookeeper", scope = SpiScope.SINGLETON)
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {
	
	@Override
	protected Registry doCreateRegistry(URL url, AbstractRegistryConfig config) {
		if (!(config instanceof ZookeeperRegistryConfig)) {
			throw new IllegalArgumentException("ZookeeperRegistryConfig is error"); 
		}
		
		ZookeeperRegistryConfig registryConfig = (ZookeeperRegistryConfig)config;
		
		Builder builder = CuratorFrameworkFactory.builder()
				.connectString(url.getParameter("address"))
		        .retryPolicy(new RetryNTimes(registryConfig.getConnAttemptNum(), registryConfig.getConnAttemptWaitTime()))  
		        .connectionTimeoutMs(registryConfig.getConnTimeout());
		
		return new ZookeeperRegistry(url, builder.build());
	}
	
}