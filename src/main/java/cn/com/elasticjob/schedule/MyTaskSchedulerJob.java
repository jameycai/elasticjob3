package cn.com.elasticjob.schedule;

import cn.com.elasticjob.bean.DeviceVo;
import cn.com.elasticjob.common.ConvertUtil;
import cn.com.elasticjob.constants.RedisKeyConstant;
import cn.com.elasticjob.schedule.elasticjob.AbstractElasticJobTask;
import cn.com.elasticjob.schedule.elasticjob.ShardingHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.Set;


/**
 * elasticjob任务调度, 启动时加载此类
 *
 *
 * @company        XXXXX股份有限公司
 * @author         caijinpeng
 * @version        V1.0
 * @Date           2018年10月16日 下午6:33:59
 */
@Configuration
@Order(value = 12)
public class MyTaskSchedulerJob extends AbstractElasticJobTask implements SimpleJob {

    private final static Logger log = LoggerFactory.getLogger(MyTaskSchedulerJob.class);

    @Value("${elasticjob.robot.cron:0/15 * * * * ?}")
    private String cron;
    @Value("${elasticjob.robot.sharding-total-count:10}")
    private String shardingTotalCount;


    /**
     * 类初化和初始方法
     */
    @Bean(initMethod = "init")
    public SimpleJob robotJob() {
        return new MyTaskSchedulerJob();
    }

    /**
     * 初始方法
     */
    public void init() {
        addElasticJob();
        reLoadJobTasks();
        updateZnodeListen();
    }

    /**
     * 添加到ElasticJob任务调度并取选举节点
     */
    public void addElasticJob() {
        elasticJobConfig.springJobScheduler(this, cron, shardingTotalCount);
        elasticJobConfig.collectionLeaderElection(this.getClass().getName());
    }

    /**
     * 任务分片执行
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        shardingTasks(shardingContext);
    }

    /**
     * 执行下发任务
     */
    @Override
    protected void taskToDispatch(DeviceVo devVo) {
        log.info("Robot task dispatch: {}", devVo);
        taskDispatchService.addTask(devVo);
    }



    /**
     * 加载全部采集任务，防止单一加载失败的任务
     */
    public void reLoadJobTasks() {
        try {
            int i_shardingTotalCount = ConvertUtil.Obj2int(shardingTotalCount);
            Map<Integer, String> localShardingMap = getLocalShardingMap();

            // todo 加载所有任务
            DeviceVo devVo = new DeviceVo();
            devVo.setTtJobId("test-11222");
            devVo.setId("11222");
            devVo.setProductId("11222");


            long did = ConvertUtil.Obj2long(devVo.getProductId());

            int shard = ShardingHelper.getCalcShard(did, i_shardingTotalCount) ;
            if(null!=devVo && localShardingMap.containsKey(shard) ) {
                log.info("--------------- elasticJob reLoadJobTasks --------------  productId/deviceId: {},  taskid: {}", did, devVo.getTtJobId());
                jobTaskMap.put(devVo.getTtJobId(), devVo);
            }else{
                log.info("reLoadJobTasks skip task: {},  is different. ", devVo);
            }
        } catch (Exception e) {
            log.warn("reLoadJobTasks(): Periodic(1 day) load taskList to Scheduler from db exception!", e);
        }
    }


    /**
     * 添加任务
     * @param dev
     */
    public void addTask(DeviceVo dev){

        // todo 添加任务

//        // 添加到 jobTaskMap
//        int i_shardingTotalCount = ConvertUtil.Obj2int(shardingTotalCount);
//        Map<Integer, String> localShardingMap = getLocalShardingMap();
//
//        int i_shard = ShardingHelper.getCalcShard(ConvertUtil.Obj2long(dev.getProductId()), i_shardingTotalCount);
//        log.info("scheduleTaskProcess(): i_shard={}, localShardingMap={}", i_shard, localShardingMap);
//        if (localShardingMap.containsKey(i_shard)) {
//            log.info("--------------- elasticJob addTask --------------  productId: {},  taskid: {}", dev.getProductId(), dev.getTtJobId());
//            jobTaskMap.put(dev.getTtJobId(), dev);
//        }else {
//            jobTaskMap.remove(dev.getTtJobId());
//            taskLastTimeCacheMap.remove(dev.getTtJobId());
//        }
    }



    /**
     * 删除任务
     *
     * @param dev  任务
     */
    public void deleteTask(DeviceVo dev) {

        // todo 删除任务

//        jobTaskMap.remove(dev.getTtJobId());
//
//        // 从Redis中删除
//        JedisUtils.getInstance().del(keyStr);
//        taskLastTimeCacheMap.remove(dev.getTtJobId());
//        log.info("--------------- elasticJob deleteTask completed ! --------------  productId: {},  taskid: {}", dev.getProductId(), dev.getTtJobId());
//
    }





}
