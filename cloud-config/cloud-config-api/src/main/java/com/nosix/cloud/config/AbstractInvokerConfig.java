package com.nosix.cloud.config;

import com.nosix.cloud.common.Constants;
import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.extension.SpiLoader;
import com.nosix.cloud.registry.Registry;
import com.nosix.cloud.registry.RegistryFactory;
import com.nosix.cloud.registry.support.DefaultRegistryConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractInvokerConfig<T> extends AbstractConfig {

    private String interfaceName;

    private String version;

    private String group;

    private RegistryConfig registryConfig;

    private ProtocolConfig protocolConfig;

    protected Class<T> interfaceClass;

    protected Registry getRegistry() {
        URL url = getRegistryURL(registryConfig);
        RegistryFactory registryFactory = SpiLoader.getInstance(RegistryFactory.class).getExtension(registryConfig.getProtocol());
        Registry registry = registryFactory.createRegistry(url, getRegistryConfig().getConfig() == null ? new DefaultRegistryConfig(): getRegistryConfig().getConfig());
        if(registry == null) {
            throw new IllegalArgumentException("AbstractInvokerConfig error: registry create fail");
        }
        return registry;
    }

    private URL getRegistryURL(RegistryConfig config) {
        if(config == null) {
            throw new IllegalArgumentException("AbstractInvokerConfig error: registry config is null");
        }

        if(config.getProtocol() == null) {
            throw new IllegalArgumentException("AbstractInvokerConfig error: registry config protocol is null");
        }

        if(config.getAddress() == null) {
            throw new IllegalArgumentException("AbstractInvokerConfig error: registry config address is null");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(URLParam.id.getName(), config.getId());
        paramMap.put("address", config.getAddress());
        return  new URL(config.getProtocol(), Constants.SERVICE_DEFAULT_HOST, getProtocolConfig().getPort(), interfaceName, paramMap);
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    @SuppressWarnings("unchecked")
	public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
        try {
            this.interfaceClass = (Class<T>)Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public ProtocolConfig getProtocolConfig() {
        return protocolConfig;
    }

    public void setProtocolConfig(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }
    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}
