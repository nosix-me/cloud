package com.nosix.cloud.registry.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.registry.Registry;
import com.nosix.cloud.registry.SubscribeListener;

import java.util.List;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractRegistry  implements Registry {


    public AbstractRegistry(URL url) {

    }

    protected abstract void doRegistry(URL url);
    protected abstract void doUnRegistry(URL url);
    protected abstract void doUnSubscribe(URL url, SubscribeListener listener);
    protected abstract void doSubscribe(URL url, SubscribeListener listener);
    protected abstract List<URL> doDiscover(URL url);

    public void registry(URL url) {
        if(url == null) {
            return;
        }
        doRegistry(url);
    }

    public void unRegistry(URL url) {
        if(url == null) {
            return;
        }
        doUnRegistry(url);
    }

    public void subscribe(URL url, SubscribeListener listener) {
       if(url == null) {
           return;
       }
        doSubscribe(url, listener);
    }

    public void unSubscribte(URL url, SubscribeListener listener) {
        if(url == null) {
            return;
        }
        doUnSubscribe(url, listener);
    }

    public List<URL> discover(URL url) {
        if(url == null) {
            return null;
        }
        return doDiscover(url);
    }


}
