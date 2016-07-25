package com.nosix.cloud.config.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.nosix.cloud.config.ProtocolConfig;
import com.nosix.cloud.config.ReferenceConfig;
import com.nosix.cloud.config.RegistryConfig;
import com.nosix.cloud.config.ServiceConfig;

public class CloudNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("protocol", new CloudBeanDefinitionParser(ProtocolConfig.class, true));
		registerBeanDefinitionParser("registry", new CloudBeanDefinitionParser(RegistryConfig.class, true));
		registerBeanDefinitionParser("reference", new CloudBeanDefinitionParser(ReferenceConfig.class, true));
		registerBeanDefinitionParser("service", new CloudBeanDefinitionParser(ServiceConfig.class, true));
	}

}
