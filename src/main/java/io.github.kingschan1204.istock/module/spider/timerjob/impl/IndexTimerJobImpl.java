package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.crawl.index.IndexCrawlJob;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import lombok.extern.slf4j.Slf4j;

/**
 * 指数更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class IndexTimerJobImpl implements ITimerJob{

    private IndexCrawlJob indexCrawlJob;

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if(null==indexCrawlJob){
                    log.info("开市时间开启更新线程!");
                    indexCrawlJob=new IndexCrawlJob();
                    Thread thread = new Thread(indexCrawlJob);
                    thread.start();
                }
                break;
            case STOP:
                if(null!=indexCrawlJob){
                    log.info("休市时间关闭更新线程!");
                    indexCrawlJob.stopTask();
                    indexCrawlJob=null;
                }
                break;
        }
    }
}
