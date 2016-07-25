package com.nosix.cloud.registry.zookeeper;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.util.ExceptionUtil;
import com.nosix.cloud.registry.SubscribeListener;
import com.nosix.cloud.registry.support.AbstractRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZookeeperRegistry extends AbstractRegistry {
	
	private final static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);
	private final ConcurrentMap<String, ConcurrentMap<SubscribeListener, CuratorWatcher>> zkChildListeners =
			new ConcurrentHashMap<String, ConcurrentMap<SubscribeListener, CuratorWatcher>>();
	private CuratorFramework zkClient;
	private final ReentrantLock clientLock = new ReentrantLock();
	private final ReentrantLock serverLock = new ReentrantLock();
	
	
	public ZookeeperRegistry(URL url, CuratorFramework zkClient) {
		super(url);
		this.zkClient = zkClient;
		zkClient.start();
	}


	@Override
	public void doRegistry(URL url) {
		try {
			serverLock.lock();
			ZkUtils.createNode(zkClient, ZkUtils.toRegisterPath(url));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("doRegister error:{}", ExceptionUtil.outException(e));
		} finally {
			serverLock.unlock();
		}
	}

	@Override
    public void doUnRegistry(URL url) {
		try {
			serverLock.lock();
			ZkUtils.delete(zkClient, ZkUtils.toRegisterPath(url));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("doUnregister error:{}", ExceptionUtil.outException(e));
		} finally {
			serverLock.unlock();
		}
	}

	@Override
	public void doUnSubscribe(URL url, SubscribeListener listener) {
		try {
			clientLock.lock();
			String subscribePath = ZkUtils.toSubscribePath(url);
			ConcurrentMap<SubscribeListener, CuratorWatcher> listenerMap = zkChildListeners.get(subscribePath);
			if (listenerMap != null) {
				CuratorWatcher watcher = listenerMap.get(listener);
				if (watcher != null) {
					removeTargetChildListener(subscribePath, watcher);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clientLock.unlock();
		}
	}

	@Override
	public void doSubscribe(final URL url, final SubscribeListener listener) {
		try {
			clientLock.lock();
			String subscribePath = ZkUtils.toSubscribePath(url);
			ConcurrentMap<SubscribeListener, CuratorWatcher> listenerMap = zkChildListeners.get(subscribePath);
			if (listenerMap == null) {
				zkChildListeners.putIfAbsent(subscribePath, new ConcurrentHashMap<SubscribeListener, CuratorWatcher>());
				listenerMap = zkChildListeners.get(subscribePath);
			}
			
			CuratorWatcher watcher = listenerMap.get(listener);
			if (watcher == null) {
				watcher = new CuratorWatcherImpl(new ChildListener() {
					public void childChanged(String parentPath, List<String> currentChilds) {
						ZookeeperRegistry.this.notify(url, listener, currentChilds);
					}
				});
				listenerMap.putIfAbsent(listener, watcher);
				watcher = listenerMap.get(listener);
			}
			
			addTargetChildListener(subscribePath, watcher);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clientLock.unlock();
		}
	}

	
	@Override
	public List<URL> doDiscover(URL url) {
		String subscribePath = ZkUtils.toSubscribePath(url);
		try {
			List<String> list = zkClient.getChildren().forPath(subscribePath);
			return ZkUtils.toURLList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("discover error:{}", ExceptionUtil.outException(e));
		}
		return null;
	}

	private void notify(URL url, SubscribeListener listener, List<String> childList) {
		listener.notify(ZkUtils.toURLList(childList));
	}
	
	private class CuratorWatcherImpl implements CuratorWatcher {
		private volatile ChildListener listener;
		
		public CuratorWatcherImpl(ChildListener listener) {
			this.listener = listener;
		}
		
		public void unwatch() {
			this.listener = null;
		}
		
		public void process(WatchedEvent event) throws Exception {
			if (listener != null) {
				listener.childChanged(event.getPath(), zkClient.getChildren().usingWatcher(this).forPath(event.getPath()));
			}
		}
	}
	
	private List<String> addTargetChildListener(String path, CuratorWatcher listener) {
		try {
			ZkUtils.createNode(zkClient, path);
			return zkClient.getChildren().usingWatcher(listener).forPath(path);
		} catch (NoNodeException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
	private void removeTargetChildListener(String path, CuratorWatcher listener) {
		((CuratorWatcherImpl) listener).unwatch();
	}
	
}

