package com.nosix.cloud.monitor.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.common.extension.SpiLoader;
import com.nosix.cloud.common.util.ExceptionUtil;
import com.nosix.cloud.monitor.Monitor;
import com.nosix.cloud.monitor.MonitorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class ServiceProxyHandler implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProxyHandler.class);
    private Object service;
    private URL url;
    private Monitor monitor;
    public ServiceProxyHandler(Object service, URL url) {
        this.service = service;
        this.url = url;
        MonitorFactory monitorFactory = SpiLoader.getInstance(MonitorFactory.class).getExtension(url.getParameter(URLParam.monitor.getName()));
        monitor = monitorFactory.getMonitor(url);
        monitor.start();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long stime = System.currentTimeMillis();
        this.monitor.getConcurrent(url).incrementAndGet();
        Object result = null;
        try {
            long etime = System.currentTimeMillis();
            result = method.invoke(this.service, args);
            int concurrent = monitor.getConcurrent(url).get();
            logger.debug("invoke succeed, time:{},{}", etime - stime, url.getServiceUri());
            monitor.collect(this.url, method.getName(), concurrent, etime - stime, false);
        }catch (Exception e) {
            long etime = System.currentTimeMillis() - stime;
            int concurrent = monitor.getConcurrent(url).get();
            logger.error("service proxy invoke error:{}", ExceptionUtil.outException(e));
            monitor.collect(this.url, method.getName(), concurrent, etime - stime, true);
        } finally {
            this.monitor.getConcurrent(url).decrementAndGet();
        }
        return result;
    }
}
