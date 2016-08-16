package com.nosix.cloud.example.service;

import org.apache.thrift.TException;

public class HelloServiceImpl implements HelloService.Iface{

	public String hello(String username) throws TException {
		System.out.println(username);
		return username;
	}

	public String bye(String username) throws TException {
		return username;
	}

}