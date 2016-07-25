package com.nosix.cloud.config;

import com.nosix.cloud.common.Constants;
import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.extension.SpiLoader;
import com.nosix.cloud.registry.Registry;
import com.nosix.cloud.rpc.Protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ServiceConfig<T> extends AbstractInvokerConfig<T> {

    private Integer weight;
    private T ref;

    public void service() {
        URL serviceUrl = getServiceUURL(getProtocolConfig());
        Protocol protocol = SpiLoader.getInstance(Protocol.class).getExtension(serviceUrl.getProtocol());
        protocol.setServerConfiguration(getProtocolConfig().getServerConfig());
        //start service
        protocol.service(interfaceClass, ref, serviceUrl);

        Registry registry = getRegistry();
        registry.registry(serviceUrl);
    }


    private URL getServiceUURL(ProtocolConfig config) {
        if(config == null) {
            throw new IllegalArgumentException("ServiceConfig error: protocol config is null");
        }

        if(config.getPort() == null) {
            throw new IllegalArgumentException("ServiceConfig error: service config port is null");
        }

        if(config.getTransport() == null) {
            throw new IllegalArgumentException("ServiceConfig error: service config transport is null");
        }

        if(interfaceClass == null) {
            throw new IllegalArgumentException("ServiceConfig error: service config interface is null");
        }

        if(ref == null) {
            throw new IllegalArgumentException("ServiceConfig error: service config ref is null");
        }

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(URLParam.id.getName(), config.getId());
        paramMap.put(URLParam.group.getName(), getGroup() == null ? URLParam.group.getValue() : getGroup());
        paramMap.put(URLParam.version.getName(), getVersion() == null ? URLParam.version.getValue() : getVersion());
        paramMap.put(URLParam.transport.getName(), config.getTransport() == null ? URLParam.transport.getValue() : config.getTransport());
        if(weight != null) {
            paramMap.put("weight", weight.toString());
        } else {
            paramMap.put("weight", "1");
        }
        return new URL((config.getProtocol() == null ? Constants.SPI_NAME_CLOUD : config.getProtocol()),
                config.getHost(),config.getPort(), getInterfaceName(),paramMap);
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }
}
