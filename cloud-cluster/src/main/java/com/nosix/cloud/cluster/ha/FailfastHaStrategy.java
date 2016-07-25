package com.nosix.cloud.cluster.ha;

import com.nosix.cloud.cluster.LoadBalance;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

@Spi(name = "failfast", scope = SpiScope.PROTOTYPE)
public class FailfastHaStrategy<T> extends AbstractHaStrategy<T> {
	
	@Override
	public Response invoke(Request request, LoadBalance<T> loadBalance) {
		Reference<T> reference  = loadBalance.select();
		if (null == reference) {
			return getDefaultResponse();
		}
		
		if (!reference.isAvailable()) {
			return getDefaultResponse();
		}
		
		return reference.invoke(request);		
	}
}