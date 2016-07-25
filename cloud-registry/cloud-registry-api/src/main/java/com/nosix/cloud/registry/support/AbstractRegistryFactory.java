package com.nosix.cloud.registry.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.registry.Registry;
import com.nosix.cloud.registry.RegistryFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstractRegistryFactory implements RegistryFactory {
    private static ConcurrentMap<String, Registry> registries = new ConcurrentHashMap<String, Registry>();
    private static final ReentrantLock lock = new ReentrantLock();

    public Registry createRegistry(URL url, AbstractRegistryConfig config) {
        String registryUri = url.getUri();

        try{
            lock.lock();
            Registry registry = registries.get(registryUri);
            if(registry != null) {
                return registry;
            }
            registry = doCreateRegistry(url, config);
            if(registry == null) {
                throw new RuntimeException("Create registry failed for uri:"+registryUri);
            }
            registries.put(registryUri, registry);
            return registry;
        }catch (Exception e) {
            throw new RuntimeException("Create registry failed for uri:"+registryUri);
        } finally {
             lock.unlock();
        }
    }

    protected abstract Registry doCreateRegistry(URL url, AbstractRegistryConfig config);
}
