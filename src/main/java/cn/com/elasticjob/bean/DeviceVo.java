package cn.com.elasticjob.bean;

import java.io.Serializable;

/***
 * @title: DeviceVo
 * @description:
 *
 * @company: XXXXX股份有限公司
 * @author: caijinpeng
 * @email: jamey_cai@163.com
 * @version: V1.0
 * @Date: 2022年07月19日 10:12
 **/
public class DeviceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    private String id;

    /**
     * 设备名称
     */
    private String name;
    /**
     * 标示符 (设备的标示符)
     */
    private String label;

    /** 父设备/网关设备  标示符 (网关设备label)  */
    private String gatewayLabel; // 网关设备label
    /**
     * 设备编码（如：设备SN， 设备IMEI等）
     */
    private String deviceSn;
    /**
     * 设备业务编码（根据客户业务进行编码）
     */
    private String sn;


    /** 设备型号 */
    private String deviceModel;
    /** 设备品牌 */
    private String deviceBrand;


    /** ip */
    private String ip;
    /** 端口 */
    private Integer port;

    /** 设备是否固定IP **/
    private Integer isFixedIp;
    /**
     * 产品ID
     */
    private String productId; // 产品ID
    /**
     * 客户端：执行job任务ID
     */
    private String ttJobId;
    /**
     * 客户端：任务采集时间
     */
    private long taskCollectTime;
    /**
     * 客户端：任务状态（0-运作、1-停止）
     */
    private int taskStatus = 0;
    /**
     * 对象内部传递，用于记录是否第一次调度执行
     **/
    private boolean firstExecFlag = true;
    /**
     * 客户端：执行采集任务周期（数值-单位秒, 如：30秒； 字符串-cron表达式 ）
     */
    private String interval;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGatewayLabel() {
        return gatewayLabel;
    }

    public void setGatewayLabel(String gatewayLabel) {
        this.gatewayLabel = gatewayLabel;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getIsFixedIp() {
        return isFixedIp;
    }

    public void setIsFixedIp(Integer isFixedIp) {
        this.isFixedIp = isFixedIp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTtJobId() {
        return ttJobId;
    }

    public void setTtJobId(String ttJobId) {
        this.ttJobId = ttJobId;
    }

    public long getTaskCollectTime() {
        return taskCollectTime;
    }

    public void setTaskCollectTime(long taskCollectTime) {
        this.taskCollectTime = taskCollectTime;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean isFirstExecFlag() {
        return firstExecFlag;
    }

    public void setFirstExecFlag(boolean firstExecFlag) {
        this.firstExecFlag = firstExecFlag;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
