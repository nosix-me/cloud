package com.nosix.cloud.registry.zookeeper;

import com.nosix.cloud.registry.support.AbstractRegistryConfig;

public class ZookeeperRegistryConfig extends AbstractRegistryConfig {
	
	private int connTimeout = 5000;
	private int connAttemptNum = 3;
	private int connAttemptWaitTime = 2000;
	
	public int getConnTimeout() {
		return connTimeout;
	}
	public void setConnTimeout(int connTimeout) {
		this.connTimeout = connTimeout;
	}
	public int getConnAttemptNum() {
		return connAttemptNum;
	}
	public void setConnAttemptNum(int connAttemptNum) {
		this.connAttemptNum = connAttemptNum;
	}
	public int getConnAttemptWaitTime() {
		return connAttemptWaitTime;
	}
	public void setConnAttemptWaitTime(int connAttemptWaitTime) {
		this.connAttemptWaitTime = connAttemptWaitTime;
	}
}
