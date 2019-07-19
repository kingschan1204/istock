package io.github.kingschan1204.istock.module.spider.crawl.info;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
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
public class DyCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService;
    private AtomicInteger error;

    public DyCrawlJob(AtomicInteger error) {
        scheduledExecutorService = Executors.newScheduledThreadPool(4, new MyThreadFactory("crawlerJob-dy"));
        this.error=error;
    }

    public void stopTask() {
        scheduledExecutorService.shutdown();
        Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
        String useAgent = SpringContextUtil.getProperties("spider.useagent");
        XueQiuQuoteSpider xueQiuQuoteSpider = new XueQiuQuoteSpider(useAgent,error);
        scheduledExecutorService.scheduleAtFixedRate(xueQiuQuoteSpider, 0, 1, TimeUnit.SECONDS);
    }
}
