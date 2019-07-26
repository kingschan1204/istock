package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.crawl.index.IndexCrawlJob;
import io.github.kingschan1204.istock.module.spider.schedule.ScheduleJob;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class CoreScheduleTimerJobImpl extends AbstractTimeJob {

    private ScheduleJob scheduleJob;

    public CoreScheduleTimerJobImpl(){
        name="核心调度任务负责管理所有任务调度";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if(null==scheduleJob){
                    log.info("开启核心调度任务!");
                    scheduleJob=new ScheduleJob();
                    Thread thread = new Thread(scheduleJob);
                    thread.setDaemon(true);
                    thread.start();
                    status=STATUS.RUN;
                }
                break;
            case STOP:
                if(null!=scheduleJob){
                    log.info("关闭核心调度任务!");
                    scheduleJob.stopTask();
                    scheduleJob=null;
                    status=STATUS.STOP;
                }
                break;
        }
    }
}
