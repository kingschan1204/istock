package io.github.kingschan1204.istock.module.spider.crawl.info;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chenguoxiang
 * @create 2019-03-07 11:18
 **/
@Slf4j
public class InfoCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService;

    public InfoCrawlJob() {
        scheduledExecutorService = Executors.newScheduledThreadPool(4, new MyThreadFactory("crawlerJob-info"));
    }

    public void stopTask() {
        scheduledExecutorService.shutdown();
        Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
        String useAgent = SpringContextUtil.getProperties("spider.useagent");
        ThsInfoSpider infoSpider = new ThsInfoSpider(useAgent);
        scheduledExecutorService.scheduleAtFixedRate(infoSpider, 0, 1, TimeUnit.SECONDS);
    }
}
