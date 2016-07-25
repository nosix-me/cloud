package com.nosix.cloud.registry;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.registry.support.AbstractRegistryConfig;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi
public interface RegistryFactory {

    Registry createRegistry(URL url, AbstractRegistryConfig config);
}
