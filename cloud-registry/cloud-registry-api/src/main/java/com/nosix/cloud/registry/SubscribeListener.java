package com.nosix.cloud.registry;

import com.nosix.cloud.common.URL;

import java.util.List;

public interface SubscribeListener {

	void notify(List<URL> urlList);
	
}