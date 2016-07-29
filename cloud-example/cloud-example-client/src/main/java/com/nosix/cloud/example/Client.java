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
		ReferenceConfig<HelloService.Iface> reference = ctx.getBean("helloService", ReferenceConfig.class);
		HelloService.Iface helloService = reference.reference();
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
