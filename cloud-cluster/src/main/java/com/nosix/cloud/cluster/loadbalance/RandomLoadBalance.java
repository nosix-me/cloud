package com.nosix.cloud.cluster.loadbalance;

import java.util.List;
import java.util.Random;

import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.rpc.Reference;

@Spi(name = "random", scope = SpiScope.PROTOTYPE)
public class RandomLoadBalance<T> extends AbstractLoadBalance<T> {
	private Random random = new Random();
	@Override
	public Reference<T> doSelect(List<Reference<T>> list) {
		int index = random.nextInt(list.size());
		return list.get(index);
	}
}