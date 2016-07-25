package com.nosix.cloud.config;

import com.nosix.cloud.config.service.HelloService;
import com.nosix.cloud.config.service.HelloServiceImpl;
import com.nosix.cloud.registry.zookeeper.ZookeeperRegistryConfig;
import com.nosix.cloud.transport.nifty.NiftyServerConfiguration;

public class ServiceTest {

	public static void main(String[] args) {
		ServiceConfig<HelloService.Iface> service =  new ServiceConfig<HelloService.Iface>();
		service.setGroup("test");
		service.setId("name");
		service.setInterfaceName(HelloService.Iface.class.getName());
		service.setRef(new HelloServiceImpl());
		service.setVersion("1.0.0");
		service.setWeight(1);
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setCluster("default");
		protocolConfig.setHaStrategy("failover");
		protocolConfig.setLoadbalance("roundrobin");
		protocolConfig.setHost("127.0.0.1");
		protocolConfig.setPort(8888);
		protocolConfig.setProxy("jdk");
		protocolConfig.setTransport("nifty");
		protocolConfig.setProtocol("cloud");
		NiftyServerConfiguration configuration = new NiftyServerConfiguration();
		configuration.setBossCount(1);
		configuration.setWorkerCount(4);
		protocolConfig.setServerConfig(configuration);
		
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress("zk01:2181");
		registryConfig.setProtocol("zookeeper");
		registryConfig.setExtConfig(new ZookeeperRegistryConfig());
		service.setProtocolConfig(protocolConfig);
		service.setRegistryConfig(registryConfig);
		service.service();
	}

}
