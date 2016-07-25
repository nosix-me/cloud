package com.nosix.cloud.cluster;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

@Spi
public interface HaStrategy<T> {

	void setUrl(URL url);
	
	Response invoke(Request request, LoadBalance<T> loadBalance);
}