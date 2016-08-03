package com.nosix.cloud.rpc.filters;

import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.rpc.Filter;
import com.nosix.cloud.rpc.Invoker;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi(name = "accesslog")
public class AccessLogFilter implements Filter {

    private static final Logger logger  = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public Response filter(Invoker<?> invoker, Request request) {

        long stime = System.currentTimeMillis();
        Response response = invoker.invoke(request);
        long consumeTime = System.currentTimeMillis() - stime;
        logger.info("consume time is {}", consumeTime);
        return response;
    }
}
