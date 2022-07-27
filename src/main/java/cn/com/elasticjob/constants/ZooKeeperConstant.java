package cn.com.elasticjob.constants;

import cn.com.elasticjob.schedule.MyTaskSchedulerJob;

/**
 * ZooKeeper常量
 *
 * @author caijinpeng
 * @version V1.0
 * @Date 2020年3月5日 下午1:59:53
 */
public class ZooKeeperConstant {

    /** 命名空间 **/
    public static final String ZK_NAMESPACE = "elasticjob3x-lite-springboot-zk-001";

    /** zk的认证方案 **/
    public static final String ZK_AUTHSCHEME = "digest";
    public static final String ZK_USER = "wlkjAdminzk";
    public static final String ZK_PASSWORD = "Wlkj@2021zbvn1aok8i";


    /**
     * 采集器Collection 集群
     */
    public final static String PATH_PROBE_CLUSTER = "cluster";

    /**
     * 客户端调度Collection 集群分片节点
     */
    public final static String SHARDNODEPATH_SHARDING = MyTaskSchedulerJob.class.getName()+"/sharding";
    public final static String SHARDNODEPATH_SHARDING_INSTANCE = "instance";


    /**
     * 任务变化的ZNode名称
     */
    public final static String NODE_TASK_CHANGE = "taskchange";

    /**
     * 添加任务的ZNode名称
     */
    public final static String NODE_TASK_ADD = "add";

    /**
     * 更新任务的ZNode名称
     */
    public final static String NODE_TASK_UPDATE = "update";

    /**
     * 删除任务的ZNode名称
     */
    public final static String NODE_TASK_DELETE = "delete";

    /**
     * 任务类型-ROBOT任务
     */
    public final static String TASKTYPE_ROBOT = "robottask";
    /**
     * 任务变化消息批次时间
     */
    public final static String BATCHTIME = "batchtime";
    /**
     * Node节点内容标识
     */
    public final static String NODE_CONTENT = "content";

    /**
     * 定义任务消息分隔符
     */
    public final static String TASK_MSG_SPLIT = "#";


    /**
     * 记录当前组中的 客户端调度Collection
     */
    public final static String NODE_PROBE = "iotCollection";


    /**
     * 最后一次采集记录ZNode
     */
    public final static String NODE_TASK_LASTCOLLECT = "lastcollect";
    /**
     * 最后一次采集kafka元数据记录信息
     */
    public final static String NODE_TASK_LASTCOLLECT_METAINFO_KAFKA = "metainfo_kafka";

    /**
     * 任务变化的ZNode名称
     */
    public final static String NODE_TASK_MSG = "taskmsg";

    /**
     * zk采集域消息通知， 0：只本采集域通知  1：全部采集域通知
     */
    public final static int ZK_NOTIFY_TYPE_LOCAL = 0;
    public final static int ZK_NOTIFY_TYPE_ALL = 1;


}
