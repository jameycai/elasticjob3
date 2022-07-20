package cn.com.elasticjob.schedule.dispatch;

import cn.com.elasticjob.bean.DeviceVo;
import cn.com.elasticjob.common.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * 通过线程池分发任务进行采集
 *
 * @author caijinpeng
 * @Date 2018年6月20日 下午8:38:23
 */
@Service
public class TaskDispatchService {

    private static final Logger log = LoggerFactory.getLogger(TaskDispatchService.class);

    /** 默认线程打印日志间隔,5分钟打印一次 */
    private static final long PRINT_INTERVAL = 5*60*1000L;
    /**
     * 总数统计
     */
    private static long TOTAL_NUM = 0L;
    /**
     * 已处理数
     */
    private static long PROCESS_NUM = 0L;
    /**
     * 未处理数
     */
    private static int WAIT_PROCESS_NUM = 0;
    /**
     * 设备 任务
     */
    private LinkedList<DeviceVo> list = new LinkedList<>();
    /**
     * 处理线程池
     */
    private ThreadPoolExecutor threadPoolExecutor = null;
    /**
     * 接收线程池
     */
    private ThreadPoolExecutor receiveThreadPoolExecutor = null;

     // todo
//    @Resource
//    @Qualifier("httpClientExecCallTask")
//    private ExecClientCallTaskFunction execClientCallTaskFunction=null;


    public TaskDispatchService() {
        initThreadPool();
        startThreadPool();
    }

    /**
     * 加入采集对象
     *
     * @param devTask
     */
    public void addTask(DeviceVo devTask) {
        //减少加入队列的等待影响,使用线程
        receiveThreadPoolExecutor.execute(() -> addThreadTask(devTask));
    }

    /**
     * 初始化线程池
     */
    private void initThreadPool() {
        try {
            receiveThreadPoolExecutor = new ThreadPoolExecutor(
                    10,
                    Integer.MAX_VALUE,
                    5L,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(10000),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.DiscardOldestPolicy());
            //在allowCoreThreadTimeOut设置为true时，ThreadPoolExecutor的keepAliveTime参数必须大于0才生效
            receiveThreadPoolExecutor.allowCoreThreadTimeOut(true);

            log.info("Init TaskDispatchReceive thread pool, coreSize: {}, maxSize: {} .", 10, Integer.MAX_VALUE);

        } catch (Exception e) {
            log.error("Init TaskDispatchReceive thread pool error", e);
        }


        try {
            threadPoolExecutor = new ThreadPoolExecutor(
                    10,
                    Integer.MAX_VALUE,
                    5L,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(10000),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.DiscardOldestPolicy());
            //在allowCoreThreadTimeOut设置为true时，ThreadPoolExecutor的keepAliveTime参数必须大于0才生效
            threadPoolExecutor.allowCoreThreadTimeOut(true);

            log.info("Init TaskDispatchProcess thread pool, coreSize: {}, maxSize: {} .",  5,  Integer.MAX_VALUE);

        } catch (Exception e) {
            log.error("Init TaskDispatchProcess thread pool error", e);
        }
    }

    /**
     * 加入采集对象
     *
     * @param task
     */
    private void addThreadTask(DeviceVo task) {
        synchronized (list) {
            list.add(task);
            list.notifyAll();
            TOTAL_NUM++;
        }
    }

    /**
     * 启动线程池处理
     */
    private void startThreadPool() {
        ExecutorService pool = ThreadPool.newSingleThreadExecutor();
        pool.execute(() -> {
            long prevTime = System.currentTimeMillis();
            while (true) {
                try {
                    long currentTime = System.currentTimeMillis();
                    long time = currentTime - prevTime;
                    if (time >= PRINT_INTERVAL) {
                        WAIT_PROCESS_NUM = list.size();
                        log.info("Received task total num [{}], dispatched num [{}], undispatch num [{}]", TOTAL_NUM, PROCESS_NUM, WAIT_PROCESS_NUM);
                        prevTime = currentTime;
                        getStatistics();
                    }

                    DeviceVo devTask = null;
                    synchronized (list) {
                        if (!list.isEmpty()) {
                            //task = list.remove(0);
                            devTask = list.removeFirst();
                        } else {
                            list.wait(2000L);
                        }
                    }
                    if (devTask != null) {
                        // 提交任务
                        submitTask(devTask);
                    }
                    Thread.sleep(10L);
                } catch (Exception ex) {
                    log.warn("TaskDispatchProcess thread pool interrupted", ex);
                }
            }
        });
    }

    /**
     * 执行任务
     *
     * @param devTask
     * @Title: submitTask
     * @author caijinpeng
     */
    private void submitTask(DeviceVo devTask) {
        executeTask(devTask.getTtJobId(), devTask);
        PROCESS_NUM++;
    }



    /**
     * 执行线程
     *
     * @param collector
     * @param devTask
     */
    private void executeTask(String collector, DeviceVo devTask) {
        log.info("----------------- executeTask  start ------------------");
        // todo
        //execClientCallTaskFunction.execClientRequestTask(collector, devTask);

//        ExecuteTaskThread executeTaskThread = new ExecuteTaskThread(collector, devTask);
//
//        FutureTask<List<String>> futureTask = new FutureTask<>(executeTaskThread);
//        threadPoolExecutor.submit(futureTask);
    }



    /**
     * 任务和线程数统计
     *
     * @author zhigang
     */
    private void getStatistics() {
        //线程池需要执行的任务数量。
        long taskCount = threadPoolExecutor.getTaskCount();
        //线程池曾经创建过的最大线程数量。通过这个数据可以知道线程池是否满过。如等于线程池的最大大小，则表示线程池曾经满了。
        int largestPoolSize = threadPoolExecutor.getLargestPoolSize();
        //线程池的线程数量。如果线程池不销毁的话，池里的线程不会自动销毁，所以这个大小只增不减。
        int poolSize = threadPoolExecutor.getPoolSize();
        //获取活动的线程数。
        int activeCount = threadPoolExecutor.getActiveCount();
        //线程池在运行过程中已完成的任务数量。小于或等于taskCount。
        long completed = threadPoolExecutor.getCompletedTaskCount();
        log.info("Thread pool [TaskDispatchProcess] statistics -> taskCount:{}, completedTaskCount:{}, activeCount:{}, poolSize:{}, largestPoolSize:{} ",
                taskCount, completed, activeCount, poolSize, largestPoolSize);

    }

    /**
     * 线程池在运行过程中已完成的任务数量
     *
     * @return
     */
    public long getCompletedTaskCount() {
        return threadPoolExecutor.getCompletedTaskCount();
    }

    /**
     * 线程池曾经创建过的最大线程数量
     *
     * @return
     */
    public int getLargestPoolSize() {
        return threadPoolExecutor.getLargestPoolSize();
    }

    /**
     * 待处理任务数
     *
     * @return
     */
    public int getWaitProcessNum() {
        return WAIT_PROCESS_NUM;
    }
}
