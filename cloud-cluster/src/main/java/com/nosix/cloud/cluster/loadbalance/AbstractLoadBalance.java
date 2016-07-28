package com.nosix.cloud.cluster.loadbalance;

import java.util.ArrayList;
import java.util.List;

import com.nosix.cloud.cluster.LoadBalance;
import com.nosix.cloud.rpc.Reference;


public abstract class AbstractLoadBalance<T> implements LoadBalance<T> {
	private List<Reference<T>> references;

	public void refresh(List<Reference<T>> references) {
		this.references = references;
	}
	
	public Reference<T> select() {
		if (references == null || references.size() <= 0) {
			return null;
		}

		List<Reference<T>> list = getAvailableReferences();
		if (list.size() <= 0) {
			return null;
		}

		if (list.size() == 1) {
			return list.get(0);
		}

		return doSelect(list);
	}
	
	private List<Reference<T>> getAvailableReferences() {
		List<Reference<T>> availableReferences = new ArrayList<Reference<T>>();
		for (Reference<T> referer : references) {
			if (referer.isAvailable()) {
				availableReferences.add(referer);
			}
		}
		return availableReferences;
	}
	
	protected abstract Reference<T> doSelect(List<Reference<T>> list);
}