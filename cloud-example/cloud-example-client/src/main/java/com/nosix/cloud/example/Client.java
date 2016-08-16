package com.nosix.cloud.example;

import org.apache.thrift.TException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nosix.cloud.config.ReferenceConfig;
import com.nosix.cloud.example.service.HelloService;

/**
 * Hello world!
 *
 */
public class Client {
	public static void main( String[] args ){
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
		ctx.start();
		@SuppressWarnings("unchecked")
		HelloService.Iface helloService = ctx.getBean("helloService", HelloService.Iface.class);
		long stime = System.currentTimeMillis();
		for(int i = 0;i < 100000; i++) {
			try {
				String result = helloService.hello("cloud");
				System.out.println(result);
			} catch (TException e) {
				e.printStackTrace();
			}
		}
		System.out.println(System.currentTimeMillis() - stime);
	}
}
