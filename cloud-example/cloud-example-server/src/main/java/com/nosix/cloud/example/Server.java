package com.nosix.cloud.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nosix.cloud.config.ServiceConfig;
import com.nosix.cloud.example.service.HelloService;

/**
 * Hello world!
 *
 */
public class Server {
    public static void main( String[] args ) {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");  
		ctx.start();
		@SuppressWarnings("unchecked")
		ServiceConfig<HelloService.Iface> service = ctx.getBean("service", ServiceConfig.class);
		service.service();
		while(true) {
			try {
				Thread.sleep(1000*60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
}
