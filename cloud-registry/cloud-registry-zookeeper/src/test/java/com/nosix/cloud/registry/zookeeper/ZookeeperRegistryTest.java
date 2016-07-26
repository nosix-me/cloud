//package com.nosix.cloud.registry.zookeeper;
//import org.junit.Test;

//public class ZookeeperRegistryTest {
	
//	private URL registryUrl = URL.valueOf("zookeeper://127.0.0.1:0/com.nosx.cloud.registry.Registry?id=test&address=127.0.0.1");
//	private URL registerUrl1 = URL.valueOf("cloud://127.0.0.1:8888/com.nosx.cloud.server.Test?group=test&version=1.0.0");
//	private URL registerUrl2 = URL.valueOf("cloud://127.0.0.1:8889/com.nosx.cloud.server.Test?group=test&version=1.0.0");
//	private URL subscribeUrl = URL.valueOf("cloud://127.0.0.1:0/com.nosx.cloud.server.Test?group=test&version=1.0.0");
	
	
//	@Test
//	public void testRegister() {
//		RegistryFactory factory = SpiLoader.getInstance(RegistryFactory.class).getExtension(registryUrl.getProtocol());
//		Registry registry = factory.createRegistry(registryUrl, new ZookeeperRegistryConfig());
//		
//		System.out.println("=======注册服务=========");
//		registry.registry(registryUrl);
//		
//		System.out.println("========查看已有服务");
//		List<URL> urlList = registry.discover(subscribeUrl);
//		for (URL url : urlList) {
//			System.out.println("discover: " + url);
//		}
//		
//		System.out.println("==========订阅服务===========");
//		registry.subscribe(subscribeUrl, new SubscribeListener() {
//			public void notify(List<URL> urlList) {
//				for (URL url : urlList) {
//					System.out.println("notify: " + url.toFullString());
//				}
//			}
//		});
//		
//		System.out.println("=======注册服务=========");
//		registry.register(registerUrl2);
//		
//		System.out.println("========查看已有服务");
//		List<URL> urlList2 = registry.discover(subscribeUrl);
//		for (URL url : urlList2) {
//			System.out.println("discover: " + url);
//		}
//		
//		System.out.println("=======服务注销=========");
//		registry.unregister(registerUrl1);
//		registry.unregister(registerUrl2);
//	}
//}