package cn.com.elasticjob.schedule.cluster;

import cn.com.elasticjob.constants.ZooKeeperConstant;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 监听ZNode节点(其实是elasticjob节点下内容)
 *
 * @author caijinpeng
 * @date 2020/4/3 1:18
 */
public abstract class AbstractZNodeListener {

	private static final Logger log = LoggerFactory.getLogger(AbstractZNodeListener.class);

	@Resource
	private ZooKeeperRegister zooKeeperRegister;

    /**
     * 监听节点内的子节点
     */
    public void probeZnodeListen() {
//		//任务消息节点路径
//		String tenantId = "caijinpeng";
//		String groupId = "wlkj";
//		String appIdNodePath = tenantId + "/" + groupId;
//
//    	String probeNodePath = "/" + ZooKeeperConstant.PATH_PROBE_CLUSTER + "/" + appIdNodePath + "/" + ZooKeeperConstant.NODE_PROBE;
		String probeNodePath = "/" + ZooKeeperConstant.NODE_PROBE;
		try {
			@SuppressWarnings("resource")
			//该方式为监听节点下子节点变化
			final PathChildrenCache pathChildrenCache = new PathChildrenCache(zooKeeperRegister.getZkClient(), probeNodePath, true);
			pathChildrenCache.start();
	        PathChildrenCacheListener cacheListener = (client, event) -> {
	            if (null != event.getData()) {
	            	String value = new String(event.getData().getData());
	            	zNodeChange(event.getType(), value);
	            }
	        };
	        pathChildrenCache.getListenable().addListener(cacheListener);
	    } catch(Exception e) {
	    	log.error("Listen probe ZNode {} exception", probeNodePath, e);
	    }
    }

	public abstract void zNodeChange(Type type, String value);

	public abstract void zNodeChange(String value);

}
