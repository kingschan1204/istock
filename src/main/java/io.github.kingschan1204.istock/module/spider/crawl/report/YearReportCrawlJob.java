package io.github.kingschan1204.istock.module.spider.crawl.report;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenguoxiang
 * @create 2019-03-07 11:18
 **/
@Slf4j
public class YearReportCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService;
    private AtomicInteger error;

    public YearReportCrawlJob(AtomicInteger error) {
        scheduledExecutorService = Executors.newScheduledThreadPool(4, new MyThreadFactory("YearReport"));
        this.error=error;
    }

    public void stopTask() {
        scheduledExecutorService.shutdown();
        Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
        YearReportSpider yearReportSpider = new YearReportSpider(error);
        scheduledExecutorService.scheduleAtFixedRate(yearReportSpider, 0, 1, TimeUnit.SECONDS);
    }
}
