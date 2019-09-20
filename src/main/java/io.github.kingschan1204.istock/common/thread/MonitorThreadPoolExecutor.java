package io.github.kingschan1204.istock.common.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 *  线程池监控
 * @author chenguoxiang
 * @create 2019-09-19 10:26
 **/
@Slf4j
public class MonitorThreadPoolExecutor extends ThreadPoolExecutor {
    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
//        log.info("beforeExecute...");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        log.info("{}",String.format("总线程数:%s,活动线程数:%s,执行完成线程数:%s,排队线程数:%s",
                getTaskCount(),
                getActiveCount(),
                getCompletedTaskCount(),
                getQueue().size()
        ));
    }

    @Override
    protected void terminated() {
        super.terminated();
        log.error("terminated...");
    }
}
