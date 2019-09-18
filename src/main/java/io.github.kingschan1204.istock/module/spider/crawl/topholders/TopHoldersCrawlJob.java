package io.github.kingschan1204.istock.module.spider.crawl.topholders;

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
public class TopHoldersCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService ;


    public TopHoldersCrawlJob(){
        scheduledExecutorService = Executors.newScheduledThreadPool(4, new MyThreadFactory("topholders"));
    }

    public void stopTask(){
        scheduledExecutorService.shutdown();
        Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
            TopHoldersSpider topHoldersSpider = new TopHoldersSpider();
            scheduledExecutorService.scheduleAtFixedRate(topHoldersSpider, 0, 2, TimeUnit.SECONDS);
    }
}
