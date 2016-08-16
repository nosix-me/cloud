package com.nosix.cloud.config.spring.schema;

import com.nosix.cloud.config.ReferenceConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ReferenceConfigBean<T> extends ReferenceConfig<T>  implements FactoryBean<T>, InitializingBean, DisposableBean{

    @Override
    public void destroy() throws Exception {
        unReference();
    }

    @Override
    public T getObject() throws Exception {
        return getRef();
    }

    @Override
    public Class<?> getObjectType() {
        return getInterfaceClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        reference();
    }
}
