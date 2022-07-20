package cn.com.elasticjob.schedule.elasticjob;


import cn.com.elasticjob.common.ConvertUtil;
import cn.com.elasticjob.constants.ZooKeeperConstant;
import cn.com.elasticjob.schedule.cluster.ZooKeeperRegister;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.internal.election.LeaderService;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ElasticJob任务调度配置类
 * @author         caijinpeng
 * @Date           2018年6月28日 上午9:20:42
 */
@Service
@EnableScheduling
public class ElasticJobConfig {
	private static final Logger log = LoggerFactory.getLogger(ElasticJobConfig.class);
	
    /** 是否为主Probe */
	private static boolean isLeader = false;
	
	/** 是否已有主Probe */
	private boolean hasLeader = false;
	
    private LeaderService leaderService = null;


	private String zkConnect = "192.168.67.23:2181";


//	@Resource
//	private ZookeeperRegistryCenter regCenter;

//    /** 采集器collection 分组 */
//    private String groupId = "";
    
//    /** 分片算法---根据作业名的哈希值对服务器列表进行轮转的分片策略*/
//    //private final static String ROTATESERVERBYNAMEJOBSHARDINGSTRATEGY = "com.dangdang.ddframe.job.lite.api.strategy.impl.RotateServerByNameJobShardingStrategy";
//	private final static String ROTATESERVERBYNAMEJOBSHARDINGSTRATEGY = "com.dangdang.ddframe.job.lite.api.strategy.impl.OdevitySortByNameJobShardingStrategy";


	/**
	 * 任务名
	 * @Title: getJobName
	 * @param jobName
	 * @return
	 */
	private static String getJobName(String jobName) {
//		String tenantId = "caijinpeng";
//		String groupId = "wlkj";
//		String appIdNodePath = tenantId + "/" + groupId;
//		return ZooKeeperConstant.PATH_PROBE_CLUSTER + "/" + appIdNodePath + "/" + jobName;
		return jobName;
	}
	
	/**
	 * 创建任务调度
	 */
    public void springJobScheduler(SimpleJob simpleJob, final String cron, final String shardingTotalCount) {
    	try {
//			if (StringUtils.isNotEmpty(cron) && StringUtils.isNotEmpty(shardingTotalCount)) {
//				SpringJobScheduler springJobScheduler = new SpringJobScheduler(simpleJob, regCenter,
//						getLiteJobConfiguration(simpleJob.getClass(), cron, Integer.parseInt(shardingTotalCount)));
//				springJobScheduler.init();
//			}

			new ScheduleJobBootstrap(createRegistryCenter(), simpleJob, createJobConfiguration(simpleJob,cron,shardingTotalCount)).schedule();
    	} catch(Exception e) {
    		log.error("springJobScheduler(): Create job scheduler exception " + simpleJob.getClass().getCanonicalName(), e);
    	}
    }
	private CoordinatorRegistryCenter createRegistryCenter() {
		ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(zkConnect, ZooKeeperConstant.ZK_NAMESPACE);

		zookeeperConfiguration.setSessionTimeoutMilliseconds(60000);
		zookeeperConfiguration.setConnectionTimeoutMilliseconds(60000);
		int zkBaseSleepTimeMilliseconds = 3000;
		zookeeperConfiguration.setBaseSleepTimeMilliseconds(zkBaseSleepTimeMilliseconds);
		int zkMaxSleepTimeMilliseconds = 10000;
		zookeeperConfiguration.setMaxSleepTimeMilliseconds(zkMaxSleepTimeMilliseconds);
		zookeeperConfiguration.setMaxRetries(5);
		//zookeeperConfiguration.setDigest("digest:wlkjAdminzk:Wlkj@2021zbvn1aok8i");
		CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
		regCenter.init();
		return regCenter;
	}
	private static JobConfiguration createJobConfiguration(SimpleJob simpleJob, final String cron, final String shardingTotalCount) {
		//String jobClassName = jobClass.getCanonicalName();
		//String jobName = getJobName(simpleJob.getClass().getSimpleName());

		///JobConfiguration jobConfig = JobConfiguration.newBuilder("cn.com.elasticjob.schedule.MyTaskSchedulerJob", ConvertUtil.Obj2int(shardingTotalCount)).cron(cron).build();
		JobConfiguration jobConfig = JobConfiguration.newBuilder(simpleJob.getClass().getName(), ConvertUtil.Obj2int(shardingTotalCount)).cron(cron).build();
		return jobConfig;
	}
//
//    /**
//     *任务配置
//     */
//    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron, final int shardingTotalCount) {
//    	String jobClassName = jobClass.getCanonicalName();
//    	String jobName = getJobName(jobClass.getSimpleName());
//    	log.info("getLiteJobConfiguration(): Create elastic job: " + jobName + ", cron:" + cron + ", shardingCount:" + shardingTotalCount);
//        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(
//        		jobName, cron, shardingTotalCount).build(), jobClassName)).jobShardingStrategyClass(ROTATESERVERBYNAMEJOBSHARDINGSTRATEGY).overwrite(true).build();
//    }
    
	/**
	 * 主采集服务选举
	 */
	public void collectionLeaderElection(String jobName) {
		//leaderService = new LeaderService(createRegistryCenter(), "cn.com.elasticjob.schedule.MyTaskSchedulerJob");
		leaderService = new LeaderService(createRegistryCenter(), jobName);
		leaderInfo();
	}
	
	/**
	 * 主 客户端调度Collection 状态值
	 */
	@Scheduled(initialDelay=30*1000, fixedRate=300*1000)
    private void leaderInfo() {
        hasLeader = leaderService.hasLeader();
        isLeader = leaderService.isLeader();
		log.info("Current [客户端调度Collection]  has leader: " + hasLeader + ", current [客户端调度Collection] is leader: " + isLeader);
	}
	
	/**
	 * 是否为主 客户端调度Collection
	 */
	public static boolean isLeader() {
		return isLeader;
	}


}
