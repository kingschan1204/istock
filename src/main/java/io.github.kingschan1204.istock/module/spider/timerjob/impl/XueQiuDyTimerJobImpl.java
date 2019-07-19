package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.crawl.info.DyCrawlJob;
import io.github.kingschan1204.istock.module.spider.crawl.info.InfoCrawlJob;
import io.github.kingschan1204.istock.module.spider.crawl.info.XueQiuQuoteSpider;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class XueQiuDyTimerJobImpl implements ITimerJob{

    private DyCrawlJob dyCrawlJob;
    private AtomicInteger error=new AtomicInteger(0);

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if (null == dyCrawlJob) {
                    if(error.get()>10){
                        log.error("雪球token失效,错误超过10次,不执行Dy任务！");
                        return;
                    }
                    log.info("开启dy更新线程!");
                    dyCrawlJob = new DyCrawlJob(error);
                    new Thread(dyCrawlJob, "DyCrawlJob").start();
                }
                break;
            case STOP:
                if (null != dyCrawlJob) {
                    log.info("关闭Dy更新线程!");
                    dyCrawlJob.stopTask();
                    dyCrawlJob = null;
                }
                break;
        }
    }
}
