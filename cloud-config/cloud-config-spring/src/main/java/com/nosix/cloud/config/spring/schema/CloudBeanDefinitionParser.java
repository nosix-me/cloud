package com.nosix.cloud.config.spring.schema;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.nosix.cloud.config.ProtocolConfig;
import com.nosix.cloud.config.RegistryConfig;

public class CloudBeanDefinitionParser implements BeanDefinitionParser {

	private Class<?> beanClass;
	
	private boolean isRequired;
	
	private static final String CONFIG_REGISTRY = "registryConfig";
	
	private static final String CONFIG_PROTOCOL = "protocolConfig";
	
	private static final String CONFIG_CLIENT = "clientConfig";
	
	private static final String CONFIG_SERVER = "serverConfig";
	
	public CloudBeanDefinitionParser(Class<?> beanClass, boolean isRequired) {
		super();
		this.beanClass = beanClass;
		this.isRequired = isRequired;
	}

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return	parse(element, parserContext, beanClass, isRequired);
	}
	
	private static BeanDefinition parse(Element element, ParserContext parserContext, Class<?> beanClass, Boolean required) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String id = element.getAttribute("id");
			if((id == null || id.length() == 0) && required) {
				String generateBeanName = null;
			if(ProtocolConfig.class.equals(beanClass)) {
				generateBeanName = "protocol";
			} else if(RegistryConfig.class.equals(beanClass)) {
				generateBeanName = "registry";
			} else {
				generateBeanName = element.getAttribute("protocol");
			}
			if(generateBeanName == null || generateBeanName.length() == 0) {
				if(element.getTagName().contains(":") && !element.getTagName().endsWith(":")) {
					generateBeanName = element.getTagName().substring(element.getTagName().indexOf(":")+1);
				} else {
					generateBeanName = element.getAttribute("interfaceClass");
				}
			}
			if(generateBeanName == null || generateBeanName.length() == 0) {
				generateBeanName = beanClass.getName();
			}
			id = generateBeanName;
			int counter = 2;
			while(parserContext.getRegistry().containsBeanDefinition(id)) {
				id = generateBeanName + (counter++);
			}
		}
		if(id != null && id.length() > 0) {
			if(parserContext.getRegistry().containsBeanDefinition(id)) {
				throw new IllegalStateException("Duplicate spring bean id" + id);
			}
			parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
		}
		beanDefinition.getPropertyValues().addPropertyValue("id", id);
		Set<String> props = new HashSet<String>();
		for(Method setter : beanClass.getMethods()) {
			String name = setter.getName();
			if(name.length() <= 3 && !name.startsWith("set") 
					&& !Modifier.isPublic(setter.getModifiers())
					&& setter.getParameterTypes().length != 1) {
				continue;
			}
			String property = (name.substring(3, 4).toLowerCase() + name.substring(4)).replaceAll("_", "-");
			props.add(property);
			if("id".equals(property)) {
				beanDefinition.getPropertyValues().addPropertyValue("id", id);
				continue;
			}
			String value = element.getAttribute(property);
			if(CONFIG_REGISTRY.equals(property)) {
				value = "registry";
			}
			if(CONFIG_PROTOCOL.equals(property)) {
				value = "protocol";
			}
			if(StringUtils.isBlank(value)) {
				continue;
			}
			value = value.trim();
			if(value.length() == 0) {
				continue;
			}
			Object reference = null;
			if("ref".equals(property)) {
				if(parserContext.getRegistry().containsBeanDefinition(value)) {
					BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
					if(!refBean.isSingleton()) {
						throw new IllegalStateException("The exported service ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=\"" + value+ "\" scope=\"singleton\" ...>");
					}
				}
				reference = new RuntimeBeanReference(value);
			} 
			else if(CONFIG_REGISTRY.equals(property)) {
				if(parserContext.getRegistry().containsBeanDefinition(value)) {
					BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
					if(!refBean.isSingleton()) {
						throw new IllegalStateException("The registry configuration ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=\"" + value+ "\" scope=\"singleton\" ...>");
					}
				}
				reference = new RuntimeBeanReference(value);
			} else if(CONFIG_PROTOCOL.equals(property)) {
				if(parserContext.getRegistry().containsBeanDefinition(value)) {
					BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
					if(!refBean.isSingleton()) {
						throw new IllegalStateException("The protocol configuration ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=\"" + value+ "\" scope=\"singleton\" ...>");
					}
				}
				reference = new RuntimeBeanReference(value);
			} else if(CONFIG_CLIENT.equals(property)) {
				if(parserContext.getRegistry().containsBeanDefinition(value)) {
					BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
					if(!refBean.isSingleton()) {
						throw new IllegalStateException("The protocol configuration ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=\"" + value+ "\" scope=\"singleton\" ...>");
					}
				}
				reference = new RuntimeBeanReference(value);
			} else if(CONFIG_SERVER.equals(property)) {
				if(parserContext.getRegistry().containsBeanDefinition(value)) {
					BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
					if(!refBean.isSingleton()) {
						throw new IllegalStateException("The protocol configuration ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=\"" + value+ "\" scope=\"singleton\" ...>");
					}
				}
				reference = new RuntimeBeanReference(value);
			} else {
				reference = new TypedStringValue(value);
			}
			beanDefinition.getPropertyValues().addPropertyValue(property, reference);
		}
		return beanDefinition;
	}
}
