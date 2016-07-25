package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.rpc.Service;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


public class ProtocolFactory {
	/**
	 * key: host:port
	 * subkey:group/interface/version
	 */
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Service<?>>> serviceMap
						= new ConcurrentHashMap<String, ConcurrentHashMap<String, Service<?>>>();
	
	/**
	 * key: host:port
	 * subkey:group/interface/version
	 */
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Reference<?>>> referenceMap
						= new ConcurrentHashMap<String, ConcurrentHashMap<String, Reference<?>>>();
	
	public static Service<?> getService(URL url) {
		String hostAndPort = url.getHostAndPort();
		ConcurrentHashMap<String, Service<?>> map = serviceMap.get(hostAndPort);
		if (map == null) {
			return null;
		}
		
		String serviceKey = url.getServiceUri();
		return map.get(serviceKey);
	}

	public synchronized static void addService(Service<?> service) {
		URL url = service.getURL();
		String hostAndPort = url.getHostAndPort();
		ConcurrentHashMap<String, Service<?>> map = serviceMap.get(hostAndPort);
		if (map == null) {
			map = new ConcurrentHashMap<String, Service<?>>();
			serviceMap.put(hostAndPort, map);
		}
		
		String serviceKey = url.getServiceUri();
		map.putIfAbsent(serviceKey, service);
	}
	
	public synchronized static void removeService(Service<?> service) {
		removeService(service.getURL());
	}
	
	public synchronized static void removeService(URL url) {
		String hostAndPort = url.getHostAndPort();
		ConcurrentHashMap<String, Service<?>> map = serviceMap.get(hostAndPort);
		if (map == null) {
			return;
		}
		
		String serviceKey = url.getServiceUri();
		map.remove(serviceKey);
	}

	public synchronized static void destroyService(Service<?> service) {
		URL url = service.getURL();
		String hostAndPort = url.getHostAndPort();
		ConcurrentHashMap<String, Service<?>> map = serviceMap.get(hostAndPort);
		if (map == null) {
			return;
		}
		
		String serviceKey = url.getServiceUri();
		map.remove(serviceKey);
		if (map.isEmpty()) {
			service.destroy();
		}
	}
	
	public synchronized static void destroyAllService() {
		for (Entry<String, ConcurrentHashMap<String, Service<?>>> entryMap : serviceMap.entrySet()) {
			ConcurrentHashMap<String, Service<?>> map = entryMap.getValue();
			if (map == null || map.isEmpty()) {
				continue;
			}
			
			for (Entry<String, Service<?>> entry : map.entrySet()) {
				Service<?> exporter = entry.getValue();
				exporter.destroy();
			}
			
			map.clear();
		}
	}
	

	public static Reference<?> getReference(URL url) {
		String hostAndPort = url.getHostAndPort();
		ConcurrentHashMap<String, Reference<?>> map = referenceMap.get(hostAndPort);
		if (map == null) {
			return null;
		}
		
		String serviceKey = url.getServiceUri();
		return map.get(serviceKey);
	}

	public synchronized static void addReference(Reference<?> reference) {
		URL url = reference.getURL();
		String hostAndPort = url.getHostAndPort();
		ConcurrentHashMap<String, Reference<?>> map = referenceMap.get(hostAndPort);
		if (map == null) {
			map = new ConcurrentHashMap<String, Reference<?>>();
			referenceMap.put(hostAndPort, map);
		}
		
		String serviceKey = url.getServiceUri();
		map.putIfAbsent(serviceKey, reference);
	}
	
	public synchronized static void removeReference(Reference<?> reference) {
		removeReference(reference.getURL());
	}
	
	public synchronized static void removeReference(URL url) {
		String hostAndPort = url.getHostAndPort();
		ConcurrentHashMap<String, Reference<?>> map = referenceMap.get(hostAndPort);
		if (map == null) {
			return;
		}
		
		String serviceKey = url.getServiceUri();
		map.remove(serviceKey);
	}
	
	public synchronized static void destroyReference(Reference<?> reference) {
		URL url = reference.getURL();
		String hostAndPort = url.getHostAndPort();
		ConcurrentHashMap<String, Reference<?>> map = referenceMap.get(hostAndPort);
		if (map == null) {
			return;
		}
		
		String serviceKey = url.getServiceUri();
		map.remove(serviceKey);
		
		if (map.isEmpty()) {
			reference.destroy();
		}
	}
	
	public synchronized static void destroyAllReference() {
		for (Entry<String, ConcurrentHashMap<String, Reference<?>>> entryMap : referenceMap.entrySet()) {
			ConcurrentHashMap<String, Reference<?>> map = entryMap.getValue();
			if (map == null || map.isEmpty()) {
				continue;
			}
			
			for (Entry<String, Reference<?>> entry : map.entrySet()) {
				Reference<?> referer = entry.getValue();
				referer.destroy();
			}
			
			map.clear();
		}
	}
}