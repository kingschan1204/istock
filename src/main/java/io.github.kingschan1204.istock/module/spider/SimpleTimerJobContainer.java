package io.github.kingschan1204.istock.module.spider;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单的定时器执行容器模板
 *
 * @author chenguoxiang
 * @create 2019-09-18 16:20
 **/
@Slf4j
public class SimpleTimerJobContainer implements Runnable, IJobExecuteContainer {

    private ScheduledExecutorService scheduledExecutorService;
    //线程组名称
    private String threadName;
    //记录错误次数
    private AtomicInteger error;
    //具体的线程任务
    private AbstractHtmlSpider task;
    //初始延迟
    private long initialDelay;
    //周期
    private long period;
    //定时单位
    private TimeUnit unit;
    //线程池大小
    private int poolSize;

    public SimpleTimerJobContainer(AbstractHtmlSpider task, long initialDelay, long period, TimeUnit unit, String threadName,int poolSize) {
        this.task = task;
        this.initialDelay = initialDelay;
        this.period = period;
        this.unit = unit;
        this.threadName = threadName;
        this.poolSize=poolSize;
        error = new AtomicInteger(0);
        scheduledExecutorService = Executors.newScheduledThreadPool(poolSize, new MyThreadFactory(threadName));
        task.jobExecuteContainer = this;
    }


    @Override
    public void run() {
        scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    @Override
    public String top() {
        if (null == scheduledExecutorService) {
            log.error("执行容器为null !无法捕捉信息！");
            return null;
        }
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) scheduledExecutorService;
        return String.format("总线程数:%s,活动线程数:%s,执行完成线程数:%s,排队线程数:%s",
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getQueue().size()
        );
    }

    @Override
    public void shutDown() {
        log.info("关闭线程，普通模式！");
        scheduledExecutorService.shutdown();
        Thread.currentThread().interrupt();
    }

    @Override
    public void forceShutDown() {
        log.info("关闭线程，强制模式！");
        scheduledExecutorService.shutdownNow();
        Thread.currentThread().interrupt();
    }
}
