package com.nosix.cloud.transport.support;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.transport.Client;
import com.nosix.cloud.transport.Request;
import com.nosix.cloud.transport.Response;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public abstract class AbstactClientPool extends AbstractClient implements PooledObjectFactory<Client> {


    protected GenericObjectPool<Client> pool;

    public AbstactClientPool(URL url, AbstractClientConfiguration configuration) {
        super(url, configuration);
    }

    public Response write(Request request) {
        Response response = null;
        Client client = null;
        try {
            client = pool.borrowObject();
            if(client instanceof AbstractClient) {
                response = client.write(request);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(pool != null) {
                pool.returnObject(client);
            }
        }
        return null;
    }

    public abstract Client create();

    public PooledObject<Client> makeObject() throws Exception {
        return new DefaultPooledObject<Client>(create());
    }

    public void destroyObject(PooledObject<Client> p) throws Exception {
        p.getObject().close();
    }

    public boolean validateObject(PooledObject<Client> p) {
        return p.getObject().isAvailable();
    }

    public void activateObject(PooledObject<Client> p) throws Exception {
        if(!p.getObject().isAvailable()) {
            p.getObject().open();
        }
    }

    public void passivateObject(PooledObject<Client> p) throws Exception {

    }

    public boolean open() {
        if(pool != null) {
            return true;
        }
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(configuration.getMaxActive());
        config.setMaxIdle(configuration.getMaxIdle());
        config.setMinIdle(configuration.getMinIdle());
        config.setMaxWaitMillis(configuration.getMaxWaitMillis());
        config.setTestOnBorrow(true);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(false);
        config.setMinEvictableIdleTimeMillis(configuration.getIdleTime());
        config.setTimeBetweenEvictionRunsMillis(configuration.getIdleTime() * 2);
        pool = new GenericObjectPool<Client>(this, config);
        return true;
    }

    public void close() {
        if(pool != null) {
            pool.close();
            pool = null;
        }
    }

    public boolean isAvailable() {
        return true;
    }


}
