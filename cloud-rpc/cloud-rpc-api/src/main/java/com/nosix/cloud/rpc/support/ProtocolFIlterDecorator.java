package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.Constants;
import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.SpiLoader;
import com.nosix.cloud.rpc.Filter;
import com.nosix.cloud.rpc.Protocol;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.rpc.Service;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;
import com.nosix.cloud.transport.support.AbstractClientConfiguration;
import com.nosix.cloud.transport.support.AbstractServerConfiguration;

import java.util.List;


/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ProtocolFIlterDecorator implements Protocol {

    private Protocol protocol;

    public ProtocolFIlterDecorator(Protocol protocol) {
        if(protocol == null) {
            throw new IllegalArgumentException("Protocol is null when construct ProtocolFilterDecorator");
        }
        this.protocol = protocol;
    }

    @Override
    public <T> Service<T> service(T obj, URL url) {
        return protocol.service(obj,url);
    }

    @Override
    public <T> Reference<T> reference(Class<T> clz, URL url) {
        return buildInvokerChain(protocol.reference(clz, url), Constants.CONSUMER);
    }

    private <T> Reference<T> buildInvokerChain(final Reference<T> reference, String consumer) {
        Reference<T> last = reference;
        List<Filter> filters = SpiLoader.getInstance(Filter.class).getExtensions(consumer);
        if(filters.size()>0) {
            for(int i = 0; i < filters.size(); i++) {
                final Filter filter = filters.get(i);
                final Reference<T> next = last;
                last = new Reference<T>() {
                    @Override
                    public Integer getActiveCount() {
                        return reference.getActiveCount();
                    }

                    @Override
                    public Response invoke(Request request) {
                        return filter.filter(next,request);
                    }

                    @Override
                    public Class<T> getInterface() {
                        return reference.getInterface();
                    }

                    @Override
                    public void init() {
                        reference.init();
                    }

                    @Override
                    public void destroy() {
                        reference.destroy();
                    }

                    @Override
                    public boolean isAvailable() {
                        return reference.isAvailable();
                    }

                    @Override
                    public URL getURL() {
                        return reference.getURL();
                    }
                };
            }
        }

        return last;
    }

    @Override
    public void destory() {

    }

    @Override
    public void setClientConfiguration(AbstractClientConfiguration configuration) {

    }

    @Override
    public void setServerConfiguration(AbstractServerConfiguration configuration) {

    }
}
