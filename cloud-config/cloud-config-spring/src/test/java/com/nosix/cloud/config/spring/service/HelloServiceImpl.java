package com.nosix.cloud.config.spring.service;

import org.apache.thrift.TException;

public class HelloServiceImpl implements HelloService.Iface{

	public String hello(String username) throws TException {
		return username;
	}

	public String bye(String username) throws TException {
		return username;
	}

}
