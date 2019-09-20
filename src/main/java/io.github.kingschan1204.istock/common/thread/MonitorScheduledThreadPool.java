package io.github.kingschan1204.istock.common.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 *  线程池监控
 * @author chenguoxiang
 * @create 2019-09-19 10:26
 **/
@Slf4j
public class MonitorScheduledThreadPool extends ScheduledThreadPoolExecutor {


    public MonitorScheduledThreadPool(int corePoolSize) {
        super(corePoolSize);
    }

    public MonitorScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public MonitorScheduledThreadPool(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public MonitorScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
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
