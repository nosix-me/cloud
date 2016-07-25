package com.nosix.cloud.cluster.loadbalance;

import java.util.List;

import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.rpc.Reference;

@Spi(name = "activecount", scope = SpiScope.PROTOTYPE)
public class ActiveCountLoadBalance<T> extends AbstractLoadBalance<T> {

	@Override
	protected Reference<T> doSelect(List<Reference<T>> list) {
		Reference<T> result = null;
		int count = -1;
		for (Reference<T> reference : list){
			int active = reference.getActiveCount();
			if (count == -1 || count > active){
				count = active;
				result = reference;
			}
		}
		return result;
	}
}