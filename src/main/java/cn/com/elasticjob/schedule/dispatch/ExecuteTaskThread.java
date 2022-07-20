package cn.com.elasticjob.schedule.dispatch;//package cn.com.iot.device.adapter.clientTask.schedule.dispatch;
//
//
//import cn.com.iot.device.adapter.base.vo.DeviceVo;
//import cn.com.iot.device.adapter.httpProtocolClient.service.HttpRequestHandler;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Callable;
//
///**
// * 实例化通用采集类执行采集
// *
// * @author caijinpeng
// * @Date 2018年6月20日 下午8:39:10
// */
//public class ExecuteTaskThread implements Callable<List<String>> {
//
//    private static final Logger logger = LoggerFactory.getLogger(ExecuteTaskThread.class);
//
//    private HttpRequestHandler httpRequestHandler = new HttpRequestHandler();
//
//    private String collectorName = null;
//    private DeviceVo devTask = null;
//
//    public ExecuteTaskThread(String collectorName, DeviceVo devTask) {
//        this.collectorName = collectorName;
//        this.devTask = devTask;
//    }
//
//    /**
//     * 执行采集类
//     */
//    @Override
//    public List<String> call() throws Exception {
//        try {
//            logger.info("----------TJHttpClientProtocolTaskService---------- DeviceVo（HttpClientServiceInfo） id="+ devTask.getId()+", decoder="+devTask.getDecoder()+", encoder="+devTask.getEncoder() );
//
//            /** 执行HTTP请求任务 ***/
//            httpRequestHandler.execHttpClientRequestTask(devTask);
//
//        } catch (Exception e) {
//            logger.error("TJHttpClientProtocolTaskService 解析设备信息异常,设备Label:{}", devTask.getLabel(), e);
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//
//
//
//
//}
