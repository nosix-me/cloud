package com.nosix.cloud.transport.nifty;

import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.NettyServerConfigBuilder;
import com.facebook.nifty.core.NettyServerTransport;
import com.facebook.nifty.core.ThriftServerDefBuilder;
import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.monitor.simple.support.DefaultServiceProxy;
import com.nosix.cloud.transport.support.AbstractServer;
import com.nosix.cloud.transport.support.AbstractServerConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TProcessor;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class NiftyServer extends AbstractServer {


    private ThriftServerDefBuilder thriftServerDefBuilder;

    private NettyServerTransport server = null;

    public NiftyServer(Object ref, URL url, AbstractServerConfiguration configuration) {
        super(ref, url, configuration);
        init();
    }

    private void init() {
        try {
            Class<?> serviceClass = ref.getClass();
            Class<?>[] interfaces = serviceClass.getInterfaces();
            TProcessor processor = null;
            for (Class<?> clazz : interfaces) {
                String cname = clazz.getSimpleName();
                if (!cname.equals("Iface")) {
                    continue;
                }
                String serviceName = clazz.getEnclosingClass().getName();
                String pname = serviceName + "$Processor";
                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    Class<?> pclass = classLoader.loadClass(pname);
                    if (!TProcessor.class.isAssignableFrom(pclass)) {
                        continue;
                    }
                    Constructor<?> constructor = pclass.getConstructor(clazz);
                    if(StringUtils.isEmpty(url.getParameter(URLParam.monitor.getName()))) {
                        processor = (TProcessor) constructor.newInstance(ref);
                    } else {
                        processor = (TProcessor) constructor.newInstance(new DefaultServiceProxy().wrapper(ref, url));
                    }
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(processor == null) {
                throw new IllegalStateException("ref-class should implements Iface");
            }
            thriftServerDefBuilder = new ThriftServerDefBuilder().listen(url.getPort()).withProcessor(processor);
            server = new NettyServerTransport(thriftServerDefBuilder.build(),
                    defaultThriftServerConfigBuilder().build(),
                    new DefaultChannelGroup());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private NettyServerConfigBuilder defaultThriftServerConfigBuilder() throws Exception {
        try {
            NettyServerConfigBuilder configBuilder = NettyServerConfig.newBuilder();
            configBuilder.setBossThreadCount(((NiftyServerConfiguration) configuration).getBossCount());
            configBuilder.setWorkerThreadCount(((NiftyServerConfiguration)configuration).getWorkerCount());
            ExecutorService boss = newFixedThreadPool(((NiftyServerConfiguration)configuration).getBossCount());
            ExecutorService workers = newFixedThreadPool(((NiftyServerConfiguration)configuration).getWorkerCount());
            configBuilder.setBossThreadExecutor(boss);
            configBuilder.setWorkerThreadExecutor(workers);
            return configBuilder;
        } catch (Exception e) {
            throw e;
        }
    }


    public boolean open() {
        if(server == null) {
            return false;
        }
        server.start();
        return true;
    }

    public void close() {
        if(server != null) {
            try {
                server.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isAvailable() {
        if(server == null) {
            return false;
        }
        if(server.getServerChannel().isOpen() && server.getServerChannel().isConnected()) {
            return true;
        }
        return false;
    }
}
