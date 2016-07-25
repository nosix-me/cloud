package com.nosix.cloud.cluster.support;

import com.nosix.cloud.cluster.Cluster;
import com.nosix.cloud.cluster.HaStrategy;
import com.nosix.cloud.cluster.LoadBalance;
import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.SpiScope;
import com.nosix.cloud.registry.Registry;
import com.nosix.cloud.rpc.Protocol;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.rpc.support.ProtocolFactory;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi(scope = SpiScope.SINGLETON, name = "default")
public class DefaultCluster<T> implements Cluster<T> {

    private HaStrategy<T> haStrategy;

    private LoadBalance<T> loadBalance;

    private Protocol protocol;

    private Registry registry;

    private Class<T> interfaceClass;

    private URL url;

    private List<Reference<T>> oldReferenceList;


    public void setInterface(Class<T> interfaceClass) {
        this.interfaceClass  = interfaceClass;
    }

    public void init() {
        haStrategy.setUrl(url);
        registry.subscribe(url, new DefaultClusterSubscribeListener<T>(this));
        List<URL> urlList = registry.discover(url);
        refresh(urlList);
    }

    public void destroy() {
        for(Reference<T> reference : oldReferenceList) {
            ProtocolFactory.removeReference(reference);
        }
    }

    public boolean isAvailable() {
        return true;
    }

    public URL getURL() {
        return this.url;
    }

    public Response invoke(Request request) {
        return haStrategy.invoke(request, loadBalance);
    }

    public Class<T> getInterface() {
        return this.interfaceClass;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public void setLoadBalance(LoadBalance<T> loadBalance) {
        this.loadBalance = loadBalance;
    }

    public void setHaStrategy(HaStrategy<T> haStrategy) {
        this.haStrategy = haStrategy;
    }

	public void refresh(List<URL> urlList) {
        List<Reference<T>> referenceList = new ArrayList<Reference<T>>();
        for(URL url : urlList) {
            Reference<T> reference = protocol.reference(interfaceClass, url);
            referenceList.add(reference);
        }
        loadBalance.refresh(referenceList);

        if(oldReferenceList == null) {
            oldReferenceList = referenceList;
            return;
        }

        oldReferenceList.removeAll(referenceList);
        for(Reference<T> reference : oldReferenceList) {
            ProtocolFactory.removeReference(reference);
        }
    }

	public LoadBalance<?> getLoadBalance() {
		return loadBalance;
	}

	public Class<T> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}    
}
