package com.nosix.cloud.config.spring.schema;

import com.nosix.cloud.config.ServiceConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ServiceConfigBean<T> extends ServiceConfig<T> implements InitializingBean, DisposableBean {

    @Override
    public void destroy() throws Exception {
        unService();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        service();
    }
}
