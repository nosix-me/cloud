package com.nosix.cloud.transport.nifty;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.transport.Server;
import com.nosix.cloud.transport.nifty.service.HelloServiceImpl;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ServiceTest {

    public static void main(String []args) {
        NiftyFactory factory = new NiftyFactory();
        URL url = new URL("cloud","localhost", 8888, "com.nosix.cloud.transport.nifty.service.HelloService");
        NiftyServerConfiguration configuration = new NiftyServerConfiguration();
        configuration.setBossCount(1);
        configuration.setWorkerCount(4);
        HelloServiceImpl ref = new HelloServiceImpl();
        Server server = factory.doCreateServer(ref, url, configuration);
        server.open();
    }
}
