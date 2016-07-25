package com.nosix.cloud.transport.nifty;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.transport.Client;
import com.nosix.cloud.transport.support.AbstactClientPool;
import com.nosix.cloud.transport.support.AbstractClientConfiguration;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class NiftyClientPool extends AbstactClientPool {


    public NiftyClientPool(URL url, AbstractClientConfiguration configuration) {
        super(url, configuration);
    }

    @Override
    public Client create() {
        return new NiftyClient(url, configuration);
    }
}
