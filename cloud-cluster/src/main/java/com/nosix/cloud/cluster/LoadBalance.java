package com.nosix.cloud.cluster;

import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.rpc.Reference;

import java.util.List;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi
public interface LoadBalance<T> {
    void refresh(List<Reference<T>> referenceList);

    Reference<T> select();
}
