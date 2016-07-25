package com.nosix.cloud.transport.support;

import com.nosix.cloud.transport.Request;

import java.io.Serializable;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class DefaultRequest implements Serializable, Request {
    private static final long serialVersionUID = -5469201401427202293L;

    @SuppressWarnings("unused")
	private String interfaceGroup;

    private String interfaceVersion;

    private String interfaceName;

    private String methodName;

    private Object[] parameters;

    private Long requestId;

    public DefaultRequest(String interfaceGroup, String interfaceVersion, String interfaceName, String methodName) {
        this.interfaceGroup = interfaceGroup;
        this.interfaceVersion = interfaceVersion;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
    }

    public DefaultRequest(String interfaceGroup, String interfaceVersion, String interfaceName, String methodName, Object[] parameters) {
        this.interfaceGroup = interfaceGroup;
        this.interfaceVersion = interfaceVersion;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public void setInterfaceGroup(String interfaceGroup) {
        this.interfaceGroup = interfaceGroup;
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return interfaceName + "." + methodName + " requestId" + requestId;
    }
}
