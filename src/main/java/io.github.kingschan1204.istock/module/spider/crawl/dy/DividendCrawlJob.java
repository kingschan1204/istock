package io.github.kingschan1204.istock.module.spider.crawl.dy;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.spider.crawl.info.XueQiuQuoteSpider;
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
public class DividendCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService;
    private AtomicInteger error;

    public DividendCrawlJob() {
        scheduledExecutorService = Executors.newScheduledThreadPool(4, new MyThreadFactory("crawlerJob-dividend"));
//        this.error=error;
    }

    public void stopTask() {
        scheduledExecutorService.shutdown();
        Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
        DividendSpider dividendSpider = new DividendSpider();
        scheduledExecutorService.scheduleAtFixedRate(dividendSpider, 0, 6, TimeUnit.SECONDS);
    }
}
