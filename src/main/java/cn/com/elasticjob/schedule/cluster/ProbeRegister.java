package cn.com.elasticjob.schedule.cluster;


import cn.com.elasticjob.common.*;
import cn.com.elasticjob.constants.GlobalConstant;
import cn.com.elasticjob.constants.ZooKeeperConstant;
import cn.com.elasticjob.schedule.MyTaskSchedulerJob;
import cn.com.elasticjob.schedule.elasticjob.ElasticJobConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Probe启动注册/Probe记录, Probe下线处理
 *
 * @author caijinpeng
 * @Date 2018年7月27日 下午3:50:24
 */
@Service
@EnableScheduling
public class ProbeRegister extends AbstractZNodeListener {

    private static final Logger log = LoggerFactory.getLogger(ProbeRegister.class);

    @Resource
    private ZNodeUpdate zNodeUpdate;
    @Resource
    private MyTaskSchedulerJob scheduler;


    /**
     * Probe组
     */
    private String groupId = "";
    private String tenantId = "";

    /**
     * Probe的Znode路径
     */
    private String probeNodePath = "";

    private int jvmProcessId = -1;
    private String ip = "";
    private String host = "";
    private String rpcPort = "";
    private String port = "";

    private static boolean isFirst = true;

    @PostConstruct
    private void init() {
//        //任务消息节点路径
//        tenantId = "caijinpeng";
//        groupId = "wlkj";
//        String appIdNodePath = tenantId + "/" + groupId;
//
//        String appId_nodePath = tenantId + "/" + groupId;
//        probeNodePath = "/" + ZooKeeperConstant.PATH_PROBE_CLUSTER + "/" + appId_nodePath + "/" + ZooKeeperConstant.NODE_PROBE;
        probeNodePath = "/" + ZooKeeperConstant.NODE_PROBE;
        initHost();
        probeZnodeListen();
        jvmProcessId = JVMInfo.getInstance().getJvmPid2();
    }

    /**
     * 所在IP端口
     */
    private void initHost() {
        try {
            ip = InetAddressUtil.getLocalIp();
            host = InetAddressUtil.getLocalHostName();
            port = System.getProperty(GlobalConstant.CONFIG_SERVER_PORT_KEY);
            if (StringUtils.isBlank(port)) {
                Map<String, Object> properties = LocalYamlConfig.initConfigProperties("bootstrap.yml");
                if(null!=properties){
                    port = ConvertUtil.Obj2Str(properties.get("server.port"));
                }
            }
            if (StringUtils.isBlank(port)) {
                port = GlobalConstant.CONFIG_SERVER_PORT_DEFAULT_VALUE + "";
                log.warn("Get server.port is empty, set default port={}", port);
            }

            if(StringUtils.isNotBlank(port)){
                System.setProperty(GlobalConstant.CONFIG_SERVER_PORT_KEY, port);
            }
        } catch (Exception e) {
            log.warn("initHost: exception!", e);
        }
    }

    /**
     * 设置Probe的节点值到ZNode, 延迟10秒注册，每2分钟调度一次，其实只有第一次调度进行注册
     */
    @Scheduled(initialDelay = 20 * 1000L, fixedRate = 1 * 60000L)
    private void setProbeNode() {
        String zNodePath = "";
        try {
            zNodePath = probeNodePath + "/" + ip + "_" + port;
            String value = jvmProcessId + "_" + ip + "_" + port + "_" + host + "_" + (ElasticJobConfig.isLeader() ? "leader" : "");
            if (isFirst) {
                //Probe启动后创建临时节点
                zNodeUpdate.nodeCreate(zNodePath, value, CreateMode.EPHEMERAL);
                isFirst = false;
                log.info("Probe ZNode: {} create probe node, set value: {}", zNodePath, value);
            } else {
                if (zNodeUpdate.checkExists(zNodePath)) {
                    zNodeUpdate.nodeUpdate(zNodePath, value);
                    log.info("Probe ZNode: {} update probe node, set value: {}", zNodePath, value);
                } else {
                    zNodeUpdate.nodeCreate(zNodePath, value, CreateMode.EPHEMERAL);
                    log.info("Probe ZNode: {} create probe node, set value: {}", zNodePath, value);
                }
            }
        } catch (Exception e) {
            log.error("Probe ZNode: {} set probe name exception", zNodePath, e);
        }
    }



    /**
     * 有Probe上线、下线时操作相关Probe记录
     *
     * @value 192.168.26.106_56060_CZG
     */
    @Override
    public void zNodeChange(Type type, String value) {
        if (type != null && type.equals(Type.CHILD_REMOVED)) {
            //probe节点下线
            if (value != null) {
                String[] arr = value.split("_");
                if (arr.length >= 3) {
                    log.info("Delete probe: {}, port: {}, in host: {}", arr[0], arr[1], arr[2]);
                    //probeService.delete(tenantId, arr[0] + "_" + arr[2]);
                }

                //检查临时节点是否因主probe删除,而被删掉, 如果临时节点不存在, 则进行临时节点创建
                zNodeUpdate.nodeCreate(zNodeUpdate.getNodePath(1), CreateMode.EPHEMERAL);
                zNodeUpdate.nodeCreate(zNodeUpdate.getNodePath(2), CreateMode.EPHEMERAL);
                zNodeUpdate.nodeCreate(zNodeUpdate.getNodePath(3), CreateMode.EPHEMERAL);

                //重新加载任务，同时重新读取本地分片配置
                ///probeMgrService.clearAllShardingCache();
                zNodeUpdate.clearZNodeShardingCache();
                scheduler.reLoadJobTasks();
                ExecutorService pool = ThreadPool.newSingleThreadExecutor();
                pool.execute(() -> {
                    try {
                        //2分钟以后，再重新读取一次，防止因probe下线重新选举分片时间长导致任务加载不准确
                        Thread.sleep(120 * 1000L);
                        /////probeMgrService.clearAllShardingCache();
                        zNodeUpdate.clearZNodeShardingCache();
                        scheduler.reLoadJobTasks();
                        log.info("Some probe delete zNodeChange, sleep 2 min end, reload shardind and task.");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        } else if (type != null && type.equals(Type.CHILD_ADDED)) {
            //重新加载任务，同时重新读取本地分片配置
           // probeMgrService.clearAllShardingCache();
            zNodeUpdate.clearZNodeShardingCache();
            scheduler.reLoadJobTasks();
            ExecutorService pool = ThreadPool.newSingleThreadExecutor();
            pool.execute(() -> {
                try {
                    //120分钟以后，再重新读取一次，防止因probe上线重新选举分片时间长导致任务加载不准确
                    Thread.sleep(120 * 1000L);
                   /// probeMgrService.clearAllShardingCache();
                    zNodeUpdate.clearZNodeShardingCache();
                    scheduler.reLoadJobTasks();
                    log.info("Some probe add zNodeChange, sleep 2 min end, reload shardind and task.");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void zNodeChange(String value) {

    }



}
