package com.nosix.cloud.filter.support;

import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiGroup;
import com.nosix.cloud.filter.Filter;
import com.nosix.cloud.rpc.Invoker;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@SpiGroup(name = "accesslog",sequence = 100)
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
