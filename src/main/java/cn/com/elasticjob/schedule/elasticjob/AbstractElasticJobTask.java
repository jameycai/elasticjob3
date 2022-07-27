package cn.com.elasticjob.schedule.elasticjob;

import cn.com.elasticjob.bean.DeviceVo;
import cn.com.elasticjob.common.ConvertUtil;
import cn.com.elasticjob.common.DateTimeUtil;
import cn.com.elasticjob.common.InetAddressUtil;
import cn.com.elasticjob.common.LocalYamlConfig;
import cn.com.elasticjob.constants.GlobalConstant;
import cn.com.elasticjob.constants.RedisKeyConstant;
import cn.com.elasticjob.schedule.cluster.ZNodeUpdate;
import cn.com.elasticjob.schedule.dispatch.TaskDispatchService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ElasticJob任务添加/任务分片/任务时间检查/任务更新
 * @author caijinpeng
 * @date 2020/4/22 16:41
 */
public abstract class AbstractElasticJobTask {

	private static final Logger log = LoggerFactory.getLogger(AbstractElasticJobTask.class);

	/**
	 * 记录任务的上次开始采集时间  Map<task_dispatchId, 13位毫秒>
	 */
	protected static Map<String, Long> taskLastTimeCacheMap = new ConcurrentHashMap<>();
	/**
	 * 采集任务 Map<task_dispatchId, DEVICEVO>
	 */
	protected static Map<String, DeviceVo> jobTaskMap = new ConcurrentHashMap<>();

	@Resource
	protected ElasticJobConfig elasticJobConfig;
	@Resource
	protected TaskDispatchService taskDispatchService;
	@Resource
	protected ZNodeUpdate zNodeUpdate;



	/**
	 * 任务按分片进行分发
	 * @param shardingContext
	 */
	protected void shardingTasks(final ShardingContext shardingContext) {
		try{
			int lSharding = shardingContext.getShardingItem();
			//总分片数
			int iShardingTotal = shardingContext.getShardingTotalCount();
			if(iShardingTotal <= 0) {
				iShardingTotal = 10;
			}

			final int fiShardingTotal = iShardingTotal;

			this.taskCheckAndDispatch(jobTaskMap.values().stream().
					filter(task -> ShardingHelper.judgeShard(ConvertUtil.Obj2long(task.getProductId()),
							fiShardingTotal, lSharding))
					.collect(Collectors.toList()));

		}catch (Exception ex){
			log.error("shardingTasks error!", ex);
			ex.printStackTrace();
		}
	}

	/**
	 * 检查任务是否到执行时间
	 * @param devTaskList
	 * @author caijinpeng
	 */
	private void taskCheckAndDispatch(List<DeviceVo> devTaskList) {
		if(null==devTaskList){
			return;
		}
		log.info("---------- elasticJob taskCheckAndDispatch size={} " , devTaskList.size());

		// todo 根据任务判断是否执行

//		if (CollectionUtils.isNotEmpty(devTaskList)) {
//			devTaskList.stream().filter(x -> x!=null).forEach(taskVo -> {
//				try {
//					long currTime = (System.currentTimeMillis() / 1000L) * 1000L;
//					String keyStr = RedisKeyConstant.REDIS_KEY_SCHEDULE_JOBTASK+"::" + taskVo.getProductId();
//					byte[] devStr = JedisUtils.getInstance().get(keyStr.getBytes());
//					if (taskVo.getTaskStatus()==1 || null==devStr || devStr.length==0){
//						// taskStatus 任务状态（0-运作、1-停止）
//						//过滤掉已删除的任务
//						log.info("----------- elasticJob taskCheck task status is stop, skip execute. productId: {},  taskid: {}", taskVo.getProductId(), taskVo.getTtJobId());
//						jobTaskMap.remove(taskVo.getTtJobId());
//						taskLastTimeCacheMap.remove(taskVo.getTtJobId());
//					} else {
//						String key = ConvertUtil.Obj2Str(taskVo.getTtJobId()); // 任务的标识
//						Long lastCollecTime = taskLastTimeCacheMap.get(key);
//						if (lastCollecTime == null) {
//							taskLastTimeCacheMap.put(key, currTime);
//							taskVo.setTaskCollectTime(currTime);  // 任务采集时间 taskCollectTime
//							taskVo.setFirstExecFlag(true); // 对象内部传递，用于记录是否第一次调度执行  firstExecFlag
//
//							//放入线程池之前克隆对象,防止地址引用改变
//							DeviceVo cloneTask = DeviceVoUtil.clone(taskVo);
//							taskToDispatch(cloneTask);
//							log.info("------first dispatch, task={}, currTime={}", taskVo, DateTimeUtil.Millis2FormatStr(currTime, "yyyy-MM-dd HH:mm:ss"));
//						} else {
//
//							boolean execFlag = false;
//							String interval = ConvertUtil.Obj2Str(taskVo.getInterval()); // 执行采集任务周期（数值-单位秒, 如：30秒； 字符串-cron表达式 ）
////							if (CrontabCheckUtil.isCrontab(interval)) {
////								//crontab表达式判断
////								//if (DynamicSwitchReadUtil.taskCollectPrintFlag()) {
////								log.info("-------dispatch, task={}, currTime={}, crontab={}", taskVo, currTime, interval);
////								///}
////								if (CrontabCheckUtil.filterWithCronTime(interval, currTime, lastCollecTime)) {
////									execFlag = true;
////								}
////							} else {
//							//保持整秒计算
//							long det = currTime - lastCollecTime.longValue();
//							// 频率 秒
//							long frequency = ConvertUtil.Obj2int(interval, 1) * 1000L; // 转换为毫秒
//							if (det >= frequency) {
//								// 当前时间-上次采集时间大于采集频率
//								execFlag = true;
//							}
////							}
//
//							//是否需要调度执行
//							if (execFlag) {
//								log.info("--------------- elasticJob task dispatch  --------------  productId: {},  taskid: {}", taskVo.getProductId(), taskVo.getTtJobId());
//								taskLastTimeCacheMap.put(key, currTime); // 更新上次采集时间
//								taskVo.setTaskCollectTime(currTime);  // 任务采集时间 taskCollectTime
//								taskVo.setFirstExecFlag(false); // 对象内部传递，用于记录是否第一次调度执行  firstExecFlag
//
//								//放入线程池之前克隆对象,防止地址引用改变
//								DeviceVo cloneTask = DeviceVoUtil.clone(taskVo);
//								taskToDispatch(cloneTask);
//
//								//if (DynamicSwitchReadUtil.taskCollectPrintFlag()) {
//								log.info("------------- elasticJob dispatch, task={}, lastTime={}, currTime={}", taskVo,
//										DateTimeUtil.Millis2StrLong(lastCollecTime),
//										DateTimeUtil.Millis2StrLong(currTime));
//								//}
//							}
//						}
//					}
//				} catch (Throwable e) {
//					log.error("Task check thread error", e);
//				}
//
//
//			});
//		}
	}

	/**
	 * 监听任务更新ZNode节点内容
	 */
	@SuppressWarnings("resource")
	protected void updateZnodeListen() {

	}

	/**
	 * 根据ip获取本地实例的分片集合
	 */
	protected Map<Integer, String> getLocalShardingMap() {
		Map<Integer, String> localShardingMap = new HashMap<>(8);
		try {
			String ip = InetAddressUtil.getLocalIp();
			String port = System.getProperty(GlobalConstant.CONFIG_SERVER_PORT_KEY);
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

			List<String> shardindList = zNodeUpdate.getShardingByIp(ip, port);
			if(shardindList != null) {
				shardindList.stream().distinct().forEach(x->localShardingMap.put(ConvertUtil.Obj2int(x), ""));
			}
		} catch (Exception e) {
			log.error("getLocalShardingMap exception", e);
		}
		return localShardingMap;
	}


	/**
	 * 任务执行分发
	 * @param devVo
	 */
	protected abstract void taskToDispatch(DeviceVo devVo);





}
