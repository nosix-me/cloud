package com.nosix.cloud.registry.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nosix.cloud.common.Constants;
import com.nosix.cloud.common.URL;

public class ZkUtils {

	private final static Logger logger = LoggerFactory.getLogger(ZkUtils.class);

	public static void createNode(CuratorFramework zkClient, String path) {
		try {
			if (!checkExists(zkClient, path)) {
				zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
			}
		} catch (NodeExistsException e) {
			logger.info("NodeExists for {}", path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void delete(CuratorFramework zkClient, String path) {
		try {
			if (checkExists(zkClient, path)) {
				zkClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkExists(CuratorFramework zkClient, String path){
		try {
			if (zkClient.checkExists().forPath(path) != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String toSubscribePath(URL url) {
		return Constants.SEPARATOR_PATH  + Constants.NAMESPACE_REGISTRY_ZOOKEEPER + Constants.SEPARATOR_PATH +
				url.getGroup() + Constants.SEPARATOR_PATH +
				url.getPath() + Constants.SEPARATOR_PATH +
				url.getVersion();
	}
	
	public static String toRegisterPath(URL url) {
		return toSubscribePath(url) + Constants.SEPARATOR_PATH + URL.encode(url.toFullString());
	}
	
	public static List<URL> toURLList(List<String> childList) {
		List<URL> childUrlList = new ArrayList<URL>();
		for (String child : childList) {
			URL childUrl = URL.valueOf(URL.decode(child));
			childUrlList.add(childUrl);
		}
		return childUrlList;
	}
	
}

