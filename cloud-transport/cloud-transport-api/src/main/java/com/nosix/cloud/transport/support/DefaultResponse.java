package com.nosix.cloud.transport.support;

import com.nosix.cloud.transport.Response;

import java.io.Serializable;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class DefaultResponse implements Serializable, Response {

    private static final long serialVersionUID = 8159207786670388707L;
    private Long requsetId;

    private Object value;

    private boolean isException;

    public Long getRequestId() {
        return requsetId;
    }

    public void setRequestId(Long requestId) {
        this.requsetId = requestId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean getException() {
        return isException;
    }

    public void setException(boolean isException) {
        this.isException = isException;
    }
}
