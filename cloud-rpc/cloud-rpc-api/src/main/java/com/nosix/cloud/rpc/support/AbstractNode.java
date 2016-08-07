package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.rpc.Node;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractNode implements Node {

    protected URL url;

    protected volatile boolean init = false;

    public AbstractNode(URL url) {
        super();
        this.url = url;
    }

    public void init() {
        if(init) {
            return;
        }
        boolean result = doInit();
        if(result) {
            init = true;
        } else {
            init = false;
        }
    }


    public URL getURL() {
        return this.url;
    }



    protected abstract boolean doInit();
}
