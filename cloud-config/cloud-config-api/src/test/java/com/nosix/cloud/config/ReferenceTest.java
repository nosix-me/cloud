package com.nosix.cloud.config;

import org.apache.thrift.TException;

import com.nosix.cloud.config.service.HelloService;
import com.nosix.cloud.registry.zookeeper.ZookeeperRegistryConfig;
import com.nosix.cloud.transport.nifty.NiftyClientConfiguration;

public class ReferenceTest {

	public static void main(String[] args) {
		ReferenceConfig<HelloService.Iface> referenceConfig  = new ReferenceConfig<HelloService.Iface>();
		referenceConfig.setGroup("test");
		referenceConfig.setId("name");
		referenceConfig.setInterfaceName(HelloService.Iface.class.getName());
		referenceConfig.setVersion("1.0.0");
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setCluster("default");
		protocolConfig.setHaStrategy("failover");
		protocolConfig.setLoadbalance("roundrobin");
		protocolConfig.setHost("127.0.0.1");
		protocolConfig.setPort(8888);
		protocolConfig.setProxy("jdk");
		protocolConfig.setTransport("nifty");
		protocolConfig.setProtocol("cloud");
		NiftyClientConfiguration configuration = new NiftyClientConfiguration();
		configuration.setMaxActive(1);
		configuration.setMaxIdle(1);
		configuration.setMinIdle(1);
		configuration.setMaxWaitMillis(2000);
		configuration.setIdleTime(10000);
		protocolConfig.setClientConfig(configuration);
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress("zk01:2181");
		registryConfig.setId("zookeeper");
		registryConfig.setProtocol("zookeeper");
		registryConfig.setExtConfig(new ZookeeperRegistryConfig());
		referenceConfig.setRegistryConfig(registryConfig);
		referenceConfig.setProtocolConfig(protocolConfig);
		HelloService.Iface helloService = referenceConfig.reference();
		try {
			long stime = System.currentTimeMillis();
			for(int i = 0; i < 100000; i++) {
				String result = helloService.hello("cloud");
				System.out.println(result);
				result = helloService.bye("john");
				System.out.println(result);
			}
			System.out.println("耗时:"+(System.currentTimeMillis() - stime)+"ms");
		} catch (TException e) {
			e.printStackTrace();
		}
	}

}
