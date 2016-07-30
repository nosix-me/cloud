package com.nosix.cloud.example;

import com.nosix.cloud.config.ReferenceConfig;
import com.nosix.cloud.example.service.HelloService;
import org.apache.thrift.TException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ThreadClient {

    public static void main( String[] args ){

        @SuppressWarnings("resource")
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
        ctx.start();
        @SuppressWarnings("unchecked")
        ReferenceConfig<HelloService.Iface> reference = ctx.getBean("helloService", ReferenceConfig.class);
        final HelloService.Iface helloService = reference.reference();
        for(int j = 0;j < 10;j ++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0;i < 1000; i++) {
                        try {
                            String result = helloService.hello("cloud");
                            System.out.println(result);
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
        }
        try {
            Thread.sleep(1000*15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
