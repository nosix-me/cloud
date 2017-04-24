package com.nosix.cloud.rpc.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.extension.Spi;
import com.nosix.cloud.common.extension.Scope;
import com.nosix.cloud.rpc.Reference;
import com.nosix.cloud.rpc.Service;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Spi(name = "cloud", scope = Scope.PROTOTYPE)
public class DefaultProtocol extends AbstractProtocol {

	@Override
	protected <T> Service<T> doService(T obj, URL url) {
		return new DefaultService<T>(obj, url, serverConfiguration);
	}

	@Override
	protected <T> Reference<T> doReference(Class<T> clz, URL url) {
		return new DefaultRefernce<T>(clz, url, clientConfiguration);
	}

}
