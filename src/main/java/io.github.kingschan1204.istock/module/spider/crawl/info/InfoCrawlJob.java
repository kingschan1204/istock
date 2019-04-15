package io.github.kingschan1204.istock.module.spider.crawl.info;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chenguoxiang
 * @create 2019-03-07 11:18
 **/
@Slf4j
public class InfoCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService ;


    public InfoCrawlJob(){
        scheduledExecutorService = Executors.newScheduledThreadPool(4, new MyThreadFactory("crawlerJob-info"));
    }

    public void stopTask(){
        scheduledExecutorService.shutdown();
        Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
            ThsInfoSpider infoSpider = new ThsInfoSpider("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3346.9 Safari/537.36");
            scheduledExecutorService.scheduleAtFixedRate(infoSpider, 0, 1, TimeUnit.SECONDS);
    }
}
