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
		int index =  idx.getAndIncrement() % list.size();
		return list.get(index);
	}
}