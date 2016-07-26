//package com.nosix.cloud.rpc;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.nosix.cloud.common.URL;
//import com.nosix.cloud.common.URLParam;
//import com.nosix.cloud.rpc.service.HelloService;
//import com.nosix.cloud.rpc.support.DefaultProtocol;
//import com.nosix.cloud.transport.Request;
//import com.nosix.cloud.transport.Response;
//import com.nosix.cloud.transport.nifty.NiftyClientConfiguration;
//import com.nosix.cloud.transport.nifty.NiftyServerConfiguration;
//import com.nosix.cloud.transport.support.DefaultRequest;
//
//public class ClientTest {
//	
//	public static void main(String []args) {
//		NiftyClientConfiguration configuration = new NiftyClientConfiguration();
//        configuration.setMaxActive(1);
//        configuration.setMinIdle(0);
//        configuration.setMaxIdle(1);
//        configuration.setIdleTime(5000);
//        configuration.setMaxWaitMillis(2000);
//        
//        Map<String,String> para = new HashMap<String, String>();
//		para.put(URLParam.transport.getName(), "nifty");
//        URL url = new URL("cloud","localhost", 8888, "com.nosix.cloud.rpc.service.HelloService",para);
//		NiftyServerConfiguration serverConfiguration = new NiftyServerConfiguration();
//		serverConfiguration.setBossCount(1);
//		serverConfiguration.setWorkerCount(4);
//		DefaultProtocol defaultProtocol = new DefaultProtocol();
//		defaultProtocol.setClientConfiguration(configuration);
//		Reference<HelloService.Iface> reference = defaultProtocol.reference(HelloService.Iface.class, url);
//		Request request = new DefaultRequest("test", "1.0.0", HelloService.Iface.class.getName(), "java.lang.String hello(java.lang.String)");
//		request.setParameters(new Object[]{"cloud"});
//        Response response = reference.invoke(request);
//        System.out.println(response.getValue());
//	}
//}
