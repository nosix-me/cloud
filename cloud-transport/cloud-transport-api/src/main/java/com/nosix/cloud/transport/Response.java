package com.nosix.cloud.transport;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public interface Response {

    Long getRequestId();

    void setRequestId(Long requestId);

    Object getValue();

    void setValue(Object value);

    boolean getException();

    void setException(boolean exception);
}
