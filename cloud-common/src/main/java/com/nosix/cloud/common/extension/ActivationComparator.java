package com.nosix.cloud.common.extension;

import java.util.Comparator;

public class ActivationComparator<T> implements Comparator<Object> {
	
	public int compare(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        
        Activation a1 = o1.getClass().getAnnotation(Activation.class);
        Activation a2 = o2.getClass().getAnnotation(Activation.class);
        
		return a1.sequence() - a2.sequence();
	}
}