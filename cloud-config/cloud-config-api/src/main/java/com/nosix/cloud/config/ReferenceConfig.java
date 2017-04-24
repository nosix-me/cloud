package com.nosix.cloud.config;

import com.nosix.cloud.cluster.Cluster;
import com.nosix.cloud.cluster.HaStrategy;
import com.nosix.cloud.cluster.LoadBalance;
import com.nosix.cloud.cluster.support.DefaultClusterProxyHandler;
import com.nosix.cloud.common.Constants;
import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.extension.ExtentionLoader;
import com.nosix.cloud.registry.Registry;
import com.nosix.cloud.rpc.Protocol;
import com.nosix.cloud.rpc.proxy.ProxyFactory;
import com.nosix.cloud.rpc.support.ProtocolFIlterDecorator;

import java.util.HashMap;
import java.util.Map;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ReferenceConfig<T> extends AbstractInvokerConfig<T> {

    private T ref;

    private Integer retry;

    private Integer timeout;

    private Cluster<T> cluster;

    public T getRef () {
        if(ref == null) {
            ref = reference();
        }
        return ref;
    }

    @SuppressWarnings("unchecked")
	public T reference() {
        URL referenceUrl = getReferenceURL(getProtocolConfig());
        Registry registry = getRegistry();
        Protocol protocol = ExtentionLoader.getExtensionLoader(Protocol.class).getExtension(referenceUrl.getProtocol());
        protocol.setClientConfiguration(getProtocolConfig().getClientConfig());

        ProtocolFIlterDecorator protocolFIlterDecorator = new ProtocolFIlterDecorator(protocol);

        Cluster<T> cluster = ExtentionLoader.getExtensionLoader(Cluster.class).getExtension(referenceUrl.getParameter(URLParam.cluster.getName()));
        HaStrategy<T> haStrategy = ExtentionLoader.getExtensionLoader(HaStrategy.class).getExtension(referenceUrl.getParameter(URLParam.haStrategy.getName()));
        LoadBalance<T> loadBalance = ExtentionLoader.getExtensionLoader(LoadBalance.class).getExtension(referenceUrl.getParameter(URLParam.loadbalance.getName()));
        cluster.setInterface(interfaceClass);
        cluster.setRegistry(registry);
        cluster.setProtocol(protocolFIlterDecorator);
        cluster.setUrl(referenceUrl);
        cluster.setHaStrategy(haStrategy);
        cluster.setLoadBalance(loadBalance);
        cluster.init();
        ProxyFactory proxyFactory = ExtentionLoader.getExtensionLoader(ProxyFactory.class).getExtension(referenceUrl.getParameter(URLParam.proxy.getName()));
        ref = proxyFactory.getProxy(interfaceClass, new DefaultClusterProxyHandler<T>(interfaceClass, cluster));
        return ref;
    }

    public void unReference() {
        if(cluster != null) {
            cluster.destroy();
        }
    }

    private URL getReferenceURL(ProtocolConfig config) {

        if(config == null) {
            throw new IllegalArgumentException("ReferenceConfig error: protocol config is null");
        }

        if(config.getHaStrategy() == null) {
            throw new IllegalArgumentException("ReferenceConfig error: reference config haStrategy is null");
        }

        if(config.getLoadbalance() == null) {
            throw new IllegalArgumentException("ReferenceConfig error: reference config loadbalance is null");
        }

        if(config.getTransport() == null) {
            throw new IllegalArgumentException("ReferenceConfig error: reference config transport is null");
        }

        if(interfaceClass == null) {
            throw new IllegalArgumentException("ReferenceConfig error: interface  is null");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(URLParam.id.getName(), config.getId());
        paramMap.put(URLParam.group.getName(), getGroup() == null ? URLParam.group.getValue() : getGroup());
        paramMap.put(URLParam.version.getName(), getVersion() == null ? URLParam.version.getValue() : getVersion());
        paramMap.put(URLParam.cluster.getName(), config.getCluster() == null ? URLParam.cluster.getValue() : config.getCluster());
        paramMap.put(URLParam.haStrategy.getName(), config.getHaStrategy() == null ? URLParam.haStrategy.getValue() : config.getHaStrategy());
        paramMap.put(URLParam.loadbalance.getName(), config.getLoadbalance() == null ? URLParam.loadbalance.getValue() : config.getLoadbalance());
        paramMap.put(URLParam.transport.getName(), config.getTransport() == null ? URLParam.transport.getValue() : config.getTransport());
        paramMap.put(URLParam.proxy.getName(), config.getProxy() == null ? URLParam.proxy.getValue() : config.getProxy());
        paramMap.put(URLParam.timeout.getName(), getTimeout().toString() == null ? URLParam.timeout.getValue() : getTimeout().toString());
        if(retry != null) {
            paramMap.put(URLParam.retries.getName(), retry.toString());
        } else {
            paramMap.put(URLParam.retries.getName(), URLParam.retries.getValue());
        }
        return new URL((config.getProtocol() == null ? Constants.SPI_NAME_CLOUD : config.getProtocol()),
                Constants.SERVICE_DEFAULT_HOST,
                Constants.SERVICE_DEFAULT_PORT,
                getInterfaceName(),
                paramMap);
    }


    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
