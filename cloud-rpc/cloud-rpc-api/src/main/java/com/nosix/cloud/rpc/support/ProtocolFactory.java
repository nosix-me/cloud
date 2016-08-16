package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.rpc.Service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


public class ProtocolFactory {

    private static Map<String, Service<?>>   services = new ConcurrentHashMap<String, Service<?>>();
	private static Map<String, Reference<?>> references = new ConcurrentHashMap<String, Reference<?>>();
	
	public static Service<?> getService(URL url) {
		String hostAndPort = url.getHostAndPort();
		Service<?> service = services.get(hostAndPort);
        return service;
	}

	public synchronized static void addService(Service<?> service) {
		URL url = service.getURL();
		String hostAndPort = url.getHostAndPort();
        services.put(hostAndPort, service);
	}

	public synchronized static void destroyAllService() {
		for (Entry<String, Service<?>> entry : services.entrySet()) {
            entry.getValue().destroy();
		}
	}
	

	public static Reference<?> getReference(URL url) {
		String hostAndPort = url.getHostAndPort();
		Reference<?> reference = references.get(hostAndPort);
		return reference;
	}

	public synchronized static void addReference(Reference<?> reference) {
		URL url = reference.getURL();
		String hostAndPort = url.getHostAndPort();
		references.put(hostAndPort, reference);
	}
	
	public synchronized static void removeReference(Reference<?> reference) {
		removeReference(reference.getURL());

	}
	
	public synchronized static void removeReference(URL url) {
	    String hostAndPort = url.getHostAndPort();
        references.remove(hostAndPort);
	}
	
	public synchronized static void destroyReference(Reference<?> reference) {
		if(reference == null) {
		    return;
        }
	    URL url = reference.getURL();
		String hostAndPort = url.getHostAndPort();
        Reference<?> temp = references.get(hostAndPort);
		if (temp != null) {
			temp.destroy();
		} else {
		    reference.destroy();
        }
	}
	
	public synchronized static void destroyAllReference() {
		for (Entry<String, Reference<?>> entry : references.entrySet()) {
            entry.getValue().destroy();
		}
	}
}