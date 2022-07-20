package cn.com.elasticjob.schedule.cluster;

import cn.com.elasticjob.constants.ZooKeeperConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务更新ZNode
 *
 * @author caijinpeng
 * @date 2020/4/3 1:16
 */
@Configuration
@Order(value = 11)
public class ZNodeUpdate {

	private static final Logger log = LoggerFactory.getLogger(ZNodeUpdate.class);

	private String tenantId = "";
	private String groupId = "";
	private String tenantIdGroupIdNodePath = "";
	private String nodePathPrefix = "";
	
	/**
	 * 分片标识
	 */
	private String probeNodePath = "";
	private String shardNodePath = "";
	/** map<probeIp_port, sharding> */
    private static Map<String, List<String>> shardMap = new ConcurrentHashMap<>();

	/**
	 * 节点路径任务变化标识，监听到nodePath_taskMsg文件有变化之后，再从nodePath_taskAdd、nodePath_taskUpdate、nodePath_taskDelete文件获取具体变化值
	 */
	private String nodePathTaskMsg = "";
	private String nodePathTaskAdd = "";
	private String nodePathTaskUpdate = "";
	private String nodePathTaskDelete = "";


	/**
	 * 上次采集中间件信息记录(例如kafka采集，记录一下上次采集时的broker列表信息)
	 * @author caijinpeng
	 */
	private String nodePathLastCollectMetaInfoKafka = "";


	@Resource
	private ZooKeeperRegister zooKeeperRegister;
    
    /** 连接ZK的Curator客户端  */
    private CuratorFramework client;
    
    /**
     * 初始方法
     */
    @PostConstruct
    private void init() {
    	initClient();
    	initNodePath();
    	nodeBuild();
    	try {
    		//等待3秒钟
			Thread.sleep(3000L);
		} catch (Exception ex){
		}
    	initGetSharding();
    }
    
    /**
     * ZK连接的Curator客户端
     */
    private void initClient() {
		try{
			client = zooKeeperRegister.getZkClient();
		}catch (Exception ex){
			ex.printStackTrace();
		}
    }


    /**
     * 初始化ZNode路径
     */
    private void initNodePath() {
//    	//任务消息节点路径
//		tenantId = "caijinpeng";
//		groupId = "wlkj";
//		tenantIdGroupIdNodePath = tenantId + "/" + groupId;
//
//    	nodePathPrefix = "/" + ZooKeeperConstant.PATH_PROBE_CLUSTER + "/" + tenantIdGroupIdNodePath;

		probeNodePath = nodePathPrefix + "/" + ZooKeeperConstant.NODE_PROBE;

		//任务变更节点路径
		String nodePathTaskChange = nodePathPrefix + "/" + ZooKeeperConstant.NODE_TASK_CHANGE;
		nodePathTaskAdd = nodePathTaskChange + "/" + ZooKeeperConstant.NODE_TASK_ADD;
		nodePathTaskUpdate = nodePathTaskChange + "/" + ZooKeeperConstant.NODE_TASK_UPDATE;
		nodePathTaskDelete = nodePathTaskChange + "/" + ZooKeeperConstant.NODE_TASK_DELETE;

    	//分片记录节点路径
    	shardNodePath = nodePathPrefix + "/" + ZooKeeperConstant.SHARDNODEPATH_SHARDING;

    	//上次采集信息记录
		String nodePathLastCollect = nodePathPrefix + "/" + ZooKeeperConstant.NODE_TASK_LASTCOLLECT;
		nodePathLastCollectMetaInfoKafka = nodePathLastCollect + "/" + ZooKeeperConstant.NODE_TASK_LASTCOLLECT_METAINFO_KAFKA;
	}
    
    /**
     * 构建结点
     */
	private void nodeBuild() {
		//创建任务消息持久节点
		nodeCreate(probeNodePath, "", CreateMode.PERSISTENT);
		//nodeCreate(shardNodePath, "", CreateMode.PERSISTENT); // 这个在elasticjob3.x版本中要删除掉

		//创建任务变更临时节点
		nodeCreate(nodePathLastCollectMetaInfoKafka, "", CreateMode.EPHEMERAL);
	}
	
	/**
	 * 检查节点是否存在
	 * @param path
	 */
	public boolean checkExists(String path) {
		boolean existFlag = false;
		if(StringUtils.isBlank(path)) {
			log.warn("ZNode checkExists path is null!");
		}else {
			try {
				Stat stat = client.checkExists().forPath(path);
				 if(null == stat) {
					 existFlag = false;
				 }else {
					 existFlag = true;
				 }
			} catch (Exception e) {
				log.warn("ZNode checkExists exception, path: {}", path, e);
			}
		}
		
		return existFlag;
	}


	/**
	 * 创建临时结点, 一般用于临时节点没有时,进行创建, 如果该临时节点的父节点没有时,不会创建
	 * @param path 临时节点路径
	 */
	private void nodeCreate(String path) {
		try {
			if(!checkExists(path)) {
				//再次对节点是否存在进行检测
				if(!checkExists(path)) {
					client.create().forPath(path, "".getBytes());
					log.info("ZNode path: {} create.", path);
				}
			} else {
				log.info("ZNode path: {} already exist.", path);
			}
		} catch (Exception e) {
			String message = e.getMessage();
			if(message != null && message.indexOf("NodeExists") > -1){
				log.info("ZNode path: {} create(1), but NodeExists.", path);
			}else {
				log.error("ZNode path: {} create(1) exception", path, e);
			}
		}
	}

	/**
	 * 创建节点，一般用于临时节点没有时,进行创建, 如果该临时节点的父节点没有时,也会创建
	 */
	public void nodeCreate(String path, CreateMode model) {
		try {
			if(!checkExists(path)) {
				//再次对节点是否存在进行检测
				if(!checkExists(path)) {
					client.create().creatingParentsIfNeeded().withMode(model).forPath(path);
				}
			}
		} catch (Exception e) {
			String message = e.getMessage();
			if(message != null && message.indexOf("NodeExists") > -1){
				log.info("ZNode path: {} create(2), but NodeExists.", path);
			}else {
				log.error("ZNode path: {} create(2) exception", path, e);
			}
		}
	}

	/**
	 * 创建节点，并内容更新赋值
	 */
	public void nodeCreate(String path, String value, CreateMode model) {
		try {
			 if(!checkExists(path)) {
				 //再次对节点是否存在进行检测
				 if(!checkExists(path)) {
					 client.create().creatingParentsIfNeeded().withMode(model).forPath(path, value.getBytes());
				 }
			 }else {
				 nodeUpdate(path, value);
			 }
		} catch (Exception e) {
			String message = e.getMessage();
			if(message != null && message.indexOf("NodeExists") > -1){
				log.info("ZNode path: {} create(3), but NodeExists.", path);
			}else{
				log.error("ZNode path: {} create(3) exception", path, e);
			}
		}
	}




	/**
	 * 节点内容更新赋值(msg节点覆盖更新), 根据采集域Id
	 *
	 * @param notifyGroupId 指定采集域通知，如果为空，则通知所有活动采集域
	 */
	private void msgNodeUpdateForCommonByGroupId(String value, String nodePathChangeMsg, String notifyGroupId) {
		if (StringUtils.isNotEmpty(notifyGroupId) && groupId.equalsIgnoreCase(notifyGroupId.trim())) {
			//只通知本采集域
			msgNodeUpdateForCommon(value, nodePathChangeMsg);
		} else {
			//按活动采集域进行通知
			List<String> activeGroupIds = getActiveGroups();
			if(activeGroupIds != null && !activeGroupIds.isEmpty()){
				//通知所有活动采集域
				String tmpPath = "";
				for (String activeGroupId : activeGroupIds) {
					if (StringUtils.isEmpty(activeGroupId)) {
						continue;
					}
					if (groupId.equalsIgnoreCase(activeGroupId)) {
						// 通知本采集域
						msgNodeUpdateForCommon(value, nodePathChangeMsg);
					} else {
						// 通知其它采集域
						tmpPath = nodePathChangeMsg.replace(tenantIdGroupIdNodePath, tenantId + "/" + activeGroupId);
						msgNodeUpdateForCommon(value, tmpPath);
						if (StringUtils.isNotEmpty(notifyGroupId) && activeGroupId.equalsIgnoreCase(notifyGroupId.trim())) {
							// 通知其它某一个采集域，通知完，结束本轮通知
							break;
						}
					}
				}
			}else{
				log.warn("msgNodeUpdateForCommonByGroupId completed, no acticeGroupIds, so not notify. notifyGroupId: {}.", notifyGroupId);
			}
		}
	}

	/**
	 * 节点内容更新赋值(msg节点覆盖更新)
	 */
	private void msgNodeUpdateForCommon(String value, String path) {
		try {
			client.setData().forPath(path, value.getBytes());
			log.info("Update ZNode path: {}, set value: {}", path, value);
		} catch (Exception e) {
			log.error("Update ZNode error for path: {}", path, e);
		}
	}


	/**
	 * 根据类型获取节点路径
	 *
	 * @param pathType 路径类型分类
	 *        0: 任务消息标识(节点路径)    1：任务添加(节点路径)    2：任务更新(节点路径)   3：任务删除(节点路径)
	 */
	public String getNodePath(int pathType) {
		String path = nodePathTaskMsg;
		if(pathType == 1) {
			path = nodePathTaskAdd;
		}else if(pathType == 2) {
			path = nodePathTaskUpdate;
		}else if(pathType == 3) {
			path = nodePathTaskDelete;
		}
		return path;
	}



	/**
	 * 获取活动采集域(5分钟之内的)
	 */
	public List<String> getActiveGroups(){
		List<String> groupIdList = new ArrayList<>();
//		try {
//			//获取5分钟内活动的groupId
//			long activeTime = System.currentTimeMillis() - 5 * 60000;
//			List<String> activeGroupIdList = runListMapper.getActiveGroupIdList(tenantId, activeTime);
//			Map<String, String> existGroupIdMap = new HashMap<>();
//			for(String activeGroupId : activeGroupIdList){
//				if(StringUtils.isEmpty(activeGroupId)) {
//					continue;
//				}
//				if(!existGroupIdMap.containsKey(activeGroupId)){
//					//不存在则添加
//					existGroupIdMap.put(activeGroupId, "");
//					groupIdList.add(activeGroupId);
//				}
//
//			}
//		} catch (Exception e) {
//			log.error("getActiveGroups catch an exception: ", e);
//		}
		return groupIdList;
	}


	
	/**
	 * 节点内容更新赋值(覆盖更新)
	 */
	public synchronized void nodeUpdate(String path, String value) {
		try {
			client.setData().forPath(path, value.getBytes());
			log.info("Update ZNode path: {}, set value: {}", path, value);
		} catch (Exception e) {
			log.error("Update ZNode error for path: {}", path, e);
		}
	}



	/**
	 * 获取/RobotTaskScheduler/sharding下分片, 从zk上获取
	 *
	 * @return map<probeIp_port, shardingList>
	 */
	private Map<String, List<String>> initGetSharding()  {
		if (client != null) {
			// 先分析probe自定义注册信息
			List<String> list = null;
			try {
				//获取probe节点下列表 /iotCollection 下
				list = client.getChildren().forPath(probeNodePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, String> tmpProbeMap = new HashMap<>();
			Map<String, String> tmpProcessidMap = new HashMap<>();
			if(list != null && !list.isEmpty()) {
				list.forEach(node -> {
					try {
						String ret1 = new String(client.getData().forPath(probeNodePath + "/" + node));
						//jvm进程id, ret1原串为: 28641_192.168.95.118_45220_sgm3_leader
						String[] ret1Array = ret1.split("_");
						String jvmProcessId = ret1Array[0];
						String ip = ret1Array[1];
						tmpProbeMap.put(ip + "@-@" + jvmProcessId, node);
						tmpProcessidMap.put("@-@" + jvmProcessId, ip);
					} catch (Exception e) {
						log.error("Get ZNode path content exception, path: {}/{} ", probeNodePath, node, e);
					}
				});
			}


			//获取分片路径下节点对象
			List<String> shardlist = null;
			try {
				// /MyTaskSchedulerJob/sharding下
				shardlist = client.getChildren().forPath(shardNodePath);
			} catch (Exception e) {
				log.error("Get ZNode path content exception, path: {} ", shardNodePath, e.getMessage());
			}
			Map<String, List<String>> tmpShardMap = new ConcurrentHashMap<>();
			if(shardlist != null && !shardlist.isEmpty()) {
				shardlist.forEach(node -> {
					try {
						//获取elastic-job分片信息192.168.95.118@-@59096
						String key1 = shardNodePath + "/" + node + "/" + ZooKeeperConstant.SHARDNODEPATH_SHARDING_INSTANCE;
						String ret1 = new String(client.getData().forPath(key1));
						if(StringUtils.isNotBlank(ret1)) {
							// 获取到的值为ip_port(192.168.95.118_45220)
							String ipPort = tmpProbeMap.get(ret1);
							if(StringUtils.isEmpty(ipPort)){
								//使用probe注册或者绑定ip拼接上processid再进行分片的取值; 根本原因是: Linux多网卡ip时，elastic-job注册ip与probe注册到zk的ip不一致问题。
								String[] retArray = ret1.split("@-@");
								String ip = tmpProcessidMap.get("@-@" + retArray[1]);
								ipPort = tmpProbeMap.get(ip + "@-@" + retArray[1]);
							}
							if(ipPort != null){
								List<String> shardList = tmpShardMap.computeIfAbsent(ipPort, k -> new ArrayList<>());
								shardList.add(node);
							}
						}else{
							log.warn("Get ZNode path content is empty, path: {}/{}/{}", shardNodePath, node, ZooKeeperConstant.SHARDNODEPATH_SHARDING_INSTANCE);
						}
					} catch (Exception e) {
						log.error("Get ZNode path content exception, path: {}/{}/{}", shardNodePath, node, ZooKeeperConstant.SHARDNODEPATH_SHARDING_INSTANCE, e);
					}
				});
			}
			if(!tmpShardMap.isEmpty()){
				shardMap.clear();
				shardMap.putAll(tmpShardMap);
			}
		}
		log.info("initGetSharding(): shardMap={}", shardMap);
		return shardMap;
	}

	/**
	 * 清理分片缓存
	 */
	public void clearZNodeShardingCache() {
		shardMap.clear();
	}

	/**
	 * 根据ip获取某个实例上的分片
	 * 
	 * @param ip
	 * @return
	 */
	public List<String> getShardingByIp(String ip, String port){
		List<String> shardindList = shardMap.get(ip + "_" + port);
		if (shardindList == null || shardindList.isEmpty()) {
			initGetSharding();
			shardindList = shardMap.get(ip + "_" + port);
		}

		log.info("~~~~~~~~~ip={}, port={}, shardindList={}", ip, port, shardindList);
		return shardindList;
	}



	
	/**
	 * 获取所有分片
	 * 
	 * @param initFlag 是否清空缓存，重新获取
	 * @return map<probeIp_port, shardingList>
	 */
	public Map<String, List<String>> getAllSharding(boolean initFlag){
		if(initFlag) {
			initGetSharding();
		}
		return shardMap;
	}
	
    /**
     * 获取当前的日期时间
     * @return
     */
    private String getTime() {
		LocalDateTime currentTime = LocalDateTime.now();
		return currentTime.toLocalTime().toString();
    }


	/**
	 * 根据zk节点路径获取节点文件内容
	 * @param nodePath
	 * @return
	 */
	public String getNodePathContent(String nodePath) {
		String content = "";
		try {
			if(StringUtils.isNotBlank(nodePath)) {
				content = new String(client.getData().forPath(nodePath));
			}
		} catch (Exception e) {
			log.error("Get ZNode path content exception, path: {}", nodePath, e);
			e.printStackTrace();
		}

		return content;
	}



}
