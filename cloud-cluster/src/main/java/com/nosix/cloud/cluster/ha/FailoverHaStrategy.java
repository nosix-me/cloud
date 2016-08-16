package com.nosix.cloud.cluster.ha;

import com.nosix.cloud.cluster.LoadBalance;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

@Spi(name = "failover", scope = SpiScope.PROTOTYPE)
public class FailoverHaStrategy<T> extends AbstractHaStrategy<T> {
	@Override
	public Response invoke(Request request, LoadBalance<T> loadBalance) {

		int retryTotalCount = url.getIntParameter(URLParam.retries.getName(), Integer.parseInt(URLParam.retries.getValue()));
		int retryCount = 0;
		do {
			Reference<T> reference = loadBalance.select();

			if (reference == null) {
				retryCount ++;
				continue;
			}

			Response response = reference.invoke(request);
			if (!response.getException()) {
				return response;
			}

			retryCount++;
		} while (retryCount < retryTotalCount);

		return getDefaultResponse();
	}
}