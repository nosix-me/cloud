package com.nosix.cloud.config;

import com.nosix.cloud.common.Constants;
import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.extension.ExtentionLoader;
import com.nosix.cloud.registry.Registry;
import com.nosix.cloud.rpc.Protocol;
import com.nosix.cloud.rpc.support.ProtocolFIlterDecorator;
import com.nosix.cloud.rpc.support.ProtocolFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ServiceConfig<T> extends AbstractInvokerConfig<T> {

    private Integer weight;

    private T ref;

    private String monitor;

    public void service() {
        final URL serviceUrl = getServiceURL(getProtocolConfig());
        final Protocol protocol = ExtentionLoader.getExtensionLoader(Protocol.class).getExtension(serviceUrl.getProtocol());
        protocol.setServerConfiguration(getProtocolConfig().getServerConfig());
        ProtocolFIlterDecorator protocolFIlterDecorator = new ProtocolFIlterDecorator(protocol);
        //start service
        protocolFIlterDecorator.service(ref, serviceUrl);

        final Registry registry = getRegistry();
        registry.registry(serviceUrl);

        //优雅停机
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if(registry != null) {
                        registry.unRegistry(serviceUrl);
                    }
                    Thread.sleep(1000 * 5);
                    protocol.destory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void unService() {
        URL serviceUrl = getServiceURL(getProtocolConfig());
        Registry registry = getRegistry();
        registry.unRegistry(serviceUrl);
        try {
            Thread.sleep(1000 * 5);
            ProtocolFactory.getService(serviceUrl).destroy();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private URL getServiceURL(ProtocolConfig config) {
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
        paramMap.put(URLParam.monitor.getName(), getMonitor());
        if(weight != null) {
            paramMap.put(URLParam.weight.getName(), weight.toString());
        } else {
            paramMap.put(URLParam.weight.getName(), URLParam.weight.getValue());
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

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }
}
