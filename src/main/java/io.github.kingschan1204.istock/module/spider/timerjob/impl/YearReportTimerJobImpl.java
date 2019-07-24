package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.crawl.report.YearReportCrawlJob;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class YearReportTimerJobImpl implements ITimerJob{

    private YearReportCrawlJob yearReportJob;
    private AtomicInteger error=new AtomicInteger(0);

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if (null == yearReportJob) {
                    if(error.get()>30){
                        log.error("Year Report download 错误超过30次,不执行Dy任务！");
                        return;
                    }
                    log.info("开启Year Report更新线程!");
                    yearReportJob = new YearReportCrawlJob(error);
                    new Thread(yearReportJob, "yearReportJob").start();
                }
                break;
            case STOP:
                if (null != yearReportJob) {
                    log.info("关闭Year Report 更新线程!");
                    yearReportJob.stopTask();
                    yearReportJob = null;
                }
                break;
        }
    }
}
