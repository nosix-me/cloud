//package com.nosix.cloud.transport.nifty;
//
//import com.nosix.cloud.common.URL;
//import com.nosix.cloud.transport.Client;
//import com.nosix.cloud.transport.Request;
//import com.nosix.cloud.transport.Response;
//import com.nosix.cloud.transport.support.DefaultRequest;
//
///**
// * auther:nosix
// * nosix.me@gmail.com
// */
//public class ClientTest {
//
//    public static void main(String []args) {
//    	NiftyFactory factory = new NiftyFactory();
//        URL url = new URL("cloud","localhost", 8888, "com.nosix.cloud.transport.nifty.service.HelloService");
//        NiftyClientConfiguration configuration = new NiftyClientConfiguration();
//        configuration.setMaxActive(1);
//        configuration.setMinIdle(0);
//        configuration.setMaxIdle(1);
//        configuration.setIdleTime(5000);
//        configuration.setMaxWaitMillis(2000);
//        Client client = factory.createClient(url, configuration);
//        client.open();
//        Request resRequest = new DefaultRequest("test", "1.0.0", "com.nosix.cloud.transport.nifty.service.HelloService$Iface", "java.lang.String hello(java.lang.String)");
//        resRequest.setParameters(new Object[]{"world"});
//        Response response = client.write(resRequest);
//        System.out.println(response.getValue());
//    }
//}
