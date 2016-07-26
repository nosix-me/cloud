package com.nosix.cloud.cluster.ha;

import com.nosix.cloud.cluster.HaStrategy;
import com.nosix.cloud.common.URL;
import com.nosix.cloud.transport.Response;
import com.nosix.cloud.transport.support.DefaultResponse;

public abstract class AbstractHaStrategy<T> implements HaStrategy<T> {
	
	protected URL url;
	
	@Override
	public void setUrl(URL url) {
		this.url = url;
	}
	
	public Response getDefaultResponse() {
		Response response = new DefaultResponse();
		response.setException(true);
		response.setValue("no service provider");
		return response;
	}
}