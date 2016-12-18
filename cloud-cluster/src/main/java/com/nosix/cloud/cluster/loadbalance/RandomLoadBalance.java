package com.nosix.cloud.cluster.loadbalance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.rpc.Reference;

@Spi(name = "random", scope = SpiScope.PROTOTYPE)
public class RandomLoadBalance<T> extends AbstractLoadBalance<T> {

	private Random random = new Random();

	@Override
	public Reference<T> doSelect(List<Reference<T>> list) {

		if(list == null || list.size() == 0) {
			return null;
		}

		List<Integer> indexs = new ArrayList<Integer>();

		for(int i = 0; i < list.size(); i++) {
			Reference<T> ref = list.get(i);
			indexs.add(i);
			int weight = ref.getURL().getIntParameter(URLParam.weight.getName(), Integer.parseInt(URLParam.weight.getValue()));
			if(weight > 1) {
				while (weight == 1) {
					indexs.add(i);
					weight--;
				}
			}
		}
		Collections.shuffle(indexs);

		int index = indexs.get(random.nextInt(indexs.size()));
		return list.get(index);
	}
}