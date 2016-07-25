package com.nosix.cloud.transport;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Request {

    Long getRequestId();

    void setRequestId(Long requestId);

    String getInterfaceVersion();

    String getInterfaceName();

    String getMethodName();

    Object[] getParameters();

    void setParameters(Object[] paramters);
}
