package com.nosix.cloud.config.spring;

import org.apache.thrift.TException;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nosix.cloud.config.ReferenceConfig;
import com.nosix.cloud.config.ServiceConfig;
import com.nosix.cloud.config.spring.service.HelloService;

import junit.framework.TestCase;

public class CloudSchemaTest extends TestCase {
	
	@Test
	public void testCloudService() {
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
//		ctx.stop();
//		ctx.close();
	}
	
	@Test
	public void testCloudReference() {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");  
		ctx.start();
		@SuppressWarnings("unchecked")
		ReferenceConfig<HelloService.Iface> reference = ctx.getBean("reference", ReferenceConfig.class);
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
//		ctx.stop();
//		ctx.close();
	}
	
}