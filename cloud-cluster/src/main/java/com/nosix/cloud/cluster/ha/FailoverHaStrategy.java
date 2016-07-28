package com.nosix.cloud.cluster.ha;

import com.nosix.cloud.cluster.LoadBalance;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

@Spi(name = "failover", scope = SpiScope.PROTOTYPE)
public class FailoverHaStrategy<T> extends AbstractHaStrategy<T> {
	private static final int DEFAULT_RETRY = 3;
	@Override
	public Response invoke(Request request, LoadBalance<T> loadBalance) {

		int retryTotalCount = url.getIntParameter("retry", DEFAULT_RETRY);
		int retryCount = 0;
		do {
			Reference<T> reference = loadBalance.select();

			if (reference == null) {
				continue;
			}

			if (!reference.isAvailable()) {
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