package io.github.kingschan1204.istock.module.spider.crawl.daily;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import io.github.kingschan1204.istock.module.spider.crawl.info.ThsInfoSpider;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chenguoxiang
 * @create 2019-03-07 11:18
 **/
@Slf4j
public class DailyBasicCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService ;


    public DailyBasicCrawlJob(){
        scheduledExecutorService = Executors.newScheduledThreadPool(4, new MyThreadFactory("crawlerJob-dailyBasicSpider"));
    }

    public void stopTask(){
        scheduledExecutorService.shutdown();
        Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
        DailyBasicSpider dailyBasicSpider = new DailyBasicSpider();
            scheduledExecutorService.scheduleAtFixedRate(dailyBasicSpider, 0, 3, TimeUnit.SECONDS);
    }
}
