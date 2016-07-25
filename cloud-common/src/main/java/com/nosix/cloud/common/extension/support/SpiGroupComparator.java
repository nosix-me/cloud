package com.nosix.cloud.common.extension.support;

import com.nosix.cloud.common.extension.SpiGroup;

import java.util.Comparator;

public class SpiGroupComparator implements Comparator<Object> {

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
        
        SpiGroup a1 = o1.getClass().getAnnotation(SpiGroup.class);
        SpiGroup a2 = o2.getClass().getAnnotation(SpiGroup.class);
        
		return a1.sequence() - a2.sequence();
	}
}