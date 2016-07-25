package com.nosix.cloud.cluster.loadbalance;

import java.util.List;

import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.rpc.Reference;

@Spi(name = "activeweight", scope = SpiScope.PROTOTYPE)
public class ActiveWeightLoadBalance<T> extends AbstractLoadBalance<T>{
	
	private static final int DEFAULT_WEIGHT = 3;

	@Override
	protected Reference<T> doSelect(List<Reference<T>> list) {
		Reference<T> result = null;
		int count = -1;
		for (Reference<T> reference : list) {
			int weight = reference.getURL().getIntParameter("weight", DEFAULT_WEIGHT);
			if (count == -1 || count < weight) {
				count = weight;
				result = reference;
			}
		}
		return result;
	}
}