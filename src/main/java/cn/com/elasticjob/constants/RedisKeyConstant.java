package cn.com.elasticjob.constants;
/***
 * @title:          RedisKeyConstant
 * @description:
 *
 * @company:        XXXXX股份有限公司
 * @author:         caijinpeng
 * @email:          jamey_cai@163.com
 * @version:        V1.0
 * @Date:           2021年03月24日 12:58
 **/
public class RedisKeyConstant {


    /** ES索引文件的redis key**/
    public static final String REDIS_ES_INDEX = "TJ_Wsat::es_index";

    /**
     * 调用JOB任务缓存
     */
    public static final String REDIS_KEY_SCHEDULE_JOBTASK = "TJ_Wsat::scheduleJobTask";

    /**
     * Http客户端协议，请求token缓存
     */
    public static final String REDIS_KEY_HTTP_CLIENT_TOKEN = "TJ_Wsat::HttpClientToken";




    /**
     * 设备采集到实时原始数据key
     */
    public static final String REDIS_KEY_DEVICE_REALTIME_ORGDATA = "device_realtime_orgdata";
    /**
     * 设备实时运行状态key
     */
    public static final String REDIS_KEY_DEVICE_REALTIME_RUNING_STATUS = "device_realtime_running_status";
    /**
     * 通过mqtt采集设备，设备的mqtt的topic信息key
     */
    public static final String REDIS_KEY_DEVICE_MQTT_TOPICINFO = "device_mqtt_topicInfo";

    /**
     * 设备（客户端）离线判断缓存 (lset)
     */
    public static final String REDIS_KEY_DEVICE_CLIENT_OFFLINE_LSET = "device_client_offline_lset";



    //============================================= 设备与规则关联 (start)  =========================
    /**
     * 设备与告警规则关系key
     */
    public static final String REDIS_KEY_DEVICE_ALARMRULE_REL = "device_alarmRule_rel_data";
    /**
     * 产品与告警规则关系key
     */
    public static final String REDIS_KEY_PRODUCT_ALARMRULE_REL = "product_alarmRule_rel_data";

//    /**
//     * 产品与数据质量关系key
//     */
//    public static final String REDIS_KEY_PRODUCT_DATA_QUALITY_REL = "product_data_quality_rel";


    /**
     * 设备与场景联动规则关系key
     */
    public static final String REDIS_KEY_DEVICE_SCENELINKAGERULE_REL = "device_sceneLinkageRule_rel_data";
    /**
     * 产品与场景联动规则关系key
     */
    public static final String REDIS_KEY_PRODUCT_SCENELINKAGERULE_REL = "product_sceneLinkageRule_rel_data";
    //============================================= 设备与规则关联 (end)  =========================



    /**
     * 设备ID与设备属性的关系key
     */
    public static final String REDIS_KEY_DEVICE_DEVICEPROPERTY_REL = "TJ_Wsat::device_deviceProperty_rel";




    //============================================= 规则详细 (start)  =========================
    /**
     * 告警规则中告警条件的drools的key
     */
    public static final String REDIS_KEY_ALARMRULE_RULE_DRL = "TJ_Wsat::alarmRule_ruleDRL_data";
    /**
     * 告警规则及规则明细key
     */
    public static final String REDIS_KEY_ALARMRULE_RULEDETAIL = "TJ_Wsat::alarmRule_ruleDetail_data";


    /**
     * 场景联动规则中告警条件中drools的key
     */
    public static final String REDIS_KEY_SCENELINKAGERULE_RULE_DRL = "TJ_Wsat::sceneLinkageRule_ruleDRL_data";
    /**
     * 场景联动规则及规则明细key （下发信息 & 设备动作）
     */
    public static final String REDIS_KEY_SCENELINKAGERULE_RULEDETAIL = "TJ_Wsat::sceneLinkageRule_ruleDetail_data";


    /**
     * 场景联动规则中对应告警规则ID的key
     */
    public static final String REDIS_KEY_SCENELINKAGERULE_CORRES_ALARMRULEID = "TJ_Wsat::sceneLinkageRule_alarmRuleId";
    //============================================= 规则详细 (end)  =========================



    /**
     * 设备告警的告警发生时间key
     */
    public static final String REDIS_KEY_DEVICE_ALARM_ALARMTIME = "TJ_Wsat::device_alarm_alarmTime";
    /**
     * 设备的活动告警（未结束告警）key
     */
    public static final String REDIS_KEY_DEVICE_ACTIVE_ALARM = "TJ_Wsat::device_activeAlarm_data";


    /**
     * 设备的启用信息key
     */
    public static final String REDIS_KEYDEVICE_ENABLE_MESSAGE = "device.enable.message";
    /**
     * 设备的解析的lable key
     */
    public static final String REDIS_DEVICE_LABEL_KEY = "device.label";
    /**
     * 设备的解析的sn key
     */
    public static final String REDIS_DEVICE_SN_KEY = "device.sn";
    /**
     * mqtt协议设备的解析的数据topic
     */
    public static final String REDIS_MQTT_DATA_TOPIC_KEY = "device.data.topic";
    /**
     * mqtt协议设备的解析的状态topic
     */
    public static final String REDIS_MQTT_STATE_TOPIC_KEY = "device.state.topic";
    /**
     * mqtt协议设备的解析的状态topic
     */
    public static final String REDIS_MQTT_TASK_KEY = "device.task";




}
