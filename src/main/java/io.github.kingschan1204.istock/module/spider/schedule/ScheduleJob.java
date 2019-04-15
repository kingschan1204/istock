package io.github.kingschan1204.istock.module.spider.schedule;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chenguoxiang
 * @create 2019-03-07 11:18
 **/
@Slf4j
public class ScheduleJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService ;


    public ScheduleJob(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new MyThreadFactory("coreSchedule"));
    }

    public void stopTask(){
        scheduledExecutorService.shutdown();
        Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
            ScheduleThread scheduleJob = new ScheduleThread();
            scheduledExecutorService.scheduleAtFixedRate(scheduleJob, 0, 1, TimeUnit.MINUTES);
    }
}
