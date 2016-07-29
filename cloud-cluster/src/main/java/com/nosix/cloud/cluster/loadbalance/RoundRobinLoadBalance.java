package com.nosix.cloud.cluster.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.rpc.Reference;

@Spi(name = "roundrobin", scope = SpiScope.PROTOTYPE)
public class RoundRobinLoadBalance<T> extends AbstractLoadBalance<T> {

	private AtomicInteger idx = new AtomicInteger(0);

	@Override
	protected Reference<T> doSelect(List<Reference<T>> list) {
		if(list == null || list.size() == 0) {
			return  null;
		}
		int index = idx.incrementAndGet();
		for(int i = 0; i < list.size(); i++) {
			Reference<T> ref = list.get((i+index) % list.size());
			if(ref.isAvailable()) {
				return ref;
			}
		}
		return  null;
	}
}