package com.nosix.cloud.cluster.loadbalance;

import java.util.List;
import java.util.Random;

import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.rpc.Reference;

@Spi(name = "random", scope = SpiScope.PROTOTYPE)
public class RandomLoadBalance<T> extends AbstractLoadBalance<T> {

	@Override
	public Reference<T> doSelect(List<Reference<T>> list) {
		if(list == null || list.size() == 0) {
			return null;
		}
		int idx = (int) (Math.random() * list.size());
		for(int i = 0; i < list.size(); i++) {
			Reference<T> ref = list.get((i+idx) % list.size());
			if(ref.isAvailable()) {
				return ref;
			}
		}
		return null;
	}
}