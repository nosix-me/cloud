package com.nosix.cloud.rpc;

import java.util.HashMap;
import java.util.Map;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.rpc.service.HelloService;
import com.nosix.cloud.rpc.service.HelloServiceImpl;
import com.nosix.cloud.rpc.support.DefaultProtocol;
import com.nosix.cloud.transport.nifty.NiftyServerConfiguration;
public class SeverTest {
	public static void main(String []args) {
		Map<String,String> para = new HashMap<String, String>();
		para.put(URLParam.transport.getName(), "nifty");
        URL url = new URL("cloud","localhost", 8888, "com.nosix.cloud.transport.nifty.service.HelloService",para);
		NiftyServerConfiguration serverConfiguration = new NiftyServerConfiguration();
		serverConfiguration.setBossCount(1);
		serverConfiguration.setWorkerCount(4);
		DefaultProtocol defaultProtocol = new DefaultProtocol();
		defaultProtocol.setServerConfiguration(serverConfiguration);
		defaultProtocol.service(HelloService.Iface.class, new HelloServiceImpl(), url);
	}
}
