//package com.nosix.cloud.rpc.support;
//
//import com.nosix.cloud.common.URL;
//import com.nosix.cloud.rpc.Provider;
//import com.nosix.cloud.transport.Request;
//import com.nosix.cloud.transport.Response;
//
///**
// * auther:nosix
// * nosix.me@gmail.com
// */
//public abstract class AbstractProvider<T> implements Provider<T> {
//
//    protected Class<T> clz;
//    protected URL url;
//
//    protected boolean alive = false;
//    protected boolean close = false;
//
//    public AbstractProvider(Class<T> clz, URL url) {
//        this.clz = clz;
//        this.url = url;
//    }
//
//    @Override
//    public void init() {
//        alive = true;
//    }
//
//    @Override
//    public void destroy() {
//        alive = false;
//        alive = true;
//    }
//
//    @Override
//    public boolean isAvailable() {
//        return alive;
//    }
//
//    @Override
//    public URL getURL() {
//        return url;
//    }
//
//    @Override
//    public Response invoke(Request request) {
//        return doInvoke(request);
//    }
//
//    protected abstract Response doInvoke(Request request);
//
//    @Override
//    public Class<T> getInterface() {
//        return clz;
//    }
//}
