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


	@Autowired
	private ZookeeperRegistryCenter zookeeperRegistryCenter;


	/**
	 * 创建任务调度
	 */
    public void springJobScheduler(SimpleJob simpleJob, final String cron, final String shardingTotalCount) {
    	try {
			if (StringUtils.isNotEmpty(cron) && StringUtils.isNotEmpty(shardingTotalCount)) {

				new ScheduleJobBootstrap(zookeeperRegistryCenter, simpleJob, createJobConfiguration(simpleJob,cron,shardingTotalCount)).schedule();

			}
    	} catch(Exception e) {
    		log.error("springJobScheduler(): Create job scheduler exception " + simpleJob.getClass().getCanonicalName(), e);
    	}
    }

	private static JobConfiguration createJobConfiguration(SimpleJob simpleJob, final String cron, final String shardingTotalCount) {

		///JobConfiguration jobConfig = JobConfiguration.newBuilder("cn.com.elasticjob.schedule.MyTaskSchedulerJob", ConvertUtil.Obj2int(shardingTotalCount)).cron(cron).build();
		JobConfiguration jobConfig = JobConfiguration.newBuilder(simpleJob.getClass().getName(), ConvertUtil.Obj2int(shardingTotalCount)).cron(cron).build();
		return jobConfig;
	}

    
	/**
	 * 主采集服务选举
	 */
	public void collectionLeaderElection(String jobName) {
		//leaderService = new LeaderService(createRegistryCenter(), "cn.com.elasticjob.schedule.MyTaskSchedulerJob");
		leaderService = new LeaderService(zookeeperRegistryCenter, jobName);
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
