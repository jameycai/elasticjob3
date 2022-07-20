package cn.com.elasticjob.common;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * @author caijinpeng
 */
public class ThreadPool {
    /**
     * 使用此方式，注意自己关闭线程池
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new CustomizableThreadFactory());
    }


    public static ExecutorService newFixedThreadPool(int corePoolSize, int maximumPoolSize,
                                                     BlockingQueue<Runnable> workQueue) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, workQueue,
                new CustomizableThreadFactory());
    }

    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                new CustomizableThreadFactory());
    }

    /**
     * 使用此方式，注意不用自己关闭线程池
     */
    public static ExecutorService newSingleThreadExecutor() {
        /*return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new CustomizableThreadFactory());*/
        //使用此方式，注意不用自己关闭线程池, newSingleThreadExecutor中已经做了关闭
        return Executors.newSingleThreadExecutor();
    }

    /**
     * 使用此方式，注意自己关闭线程池
     */
    public static ExecutorService newSingleThreadExecutor(long keepAliveTime) {
        return new ThreadPoolExecutor(1, 1, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new CustomizableThreadFactory());
    }

    /**
     * 使用此方式，注意自己关闭线程池
     */
    public static ExecutorService newSingleThreadExecutor(ThreadFactory factory) {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                factory);
    }

    /**
     * 使用此方式，注意自己关闭线程池
     */
    public static ScheduledExecutorService newScheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
    }
}
