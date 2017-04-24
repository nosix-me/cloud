package com.nosix.cloud.rpc.filters;

import com.nosix.cloud.common.Constants;
import com.nosix.cloud.common.StringTools;
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
public class AccessLogFilter implements Filter {

    private static final Logger logger  = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public Response filter(Invoker<?> invoker, Request request) {
        long stime = System.currentTimeMillis();
        boolean success = false;
        try {
            Response response = invoker.invoke(request);
            success = true;
            return response;
        } finally {
            long consumeTime = System.currentTimeMillis() - stime;
            logAccess(invoker, request, consumeTime, success);
        }
    }

    private void logAccess(Invoker<?> invoker, Request request, long consumeTime, boolean success) {
        StringBuffer stringBuffer = new StringBuffer(128);
        append(stringBuffer, request.getRequestId());
        append(stringBuffer, request.getInterfaceName());
        append(stringBuffer, request.getMethodName());
        append(stringBuffer, request.getParameters());
        append(stringBuffer, success);
        append(stringBuffer, invoker.getURL().getHostAndPort());
        append(stringBuffer, consumeTime);
        logger.debug("accesslog:"+stringBuffer.toString());
    }

    private void append(StringBuffer builder, Object field) {
        if (field != null) {
            builder.append(StringTools.urlEncode(field.toString()));
        }
        builder.append(Constants.SEPERATOR_ACCESS_LOG);
    }
}
