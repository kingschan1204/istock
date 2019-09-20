package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.SimpleTimerJobContainer;
import io.github.kingschan1204.istock.module.spider.crawl.topholders.TopHoldersSpider;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 股东更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class TopHolderTimerJobImpl extends AbstractTimeJob {

    private SimpleTimerJobContainer topHoldersCrawlJob;

    public TopHolderTimerJobImpl(){
        name="前10大股东抓取任务";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if(null==topHoldersCrawlJob){
                    log.info("start topholders job...");
                    TopHoldersSpider topHoldersSpider = new TopHoldersSpider();
                    topHoldersCrawlJob=new SimpleTimerJobContainer(topHoldersSpider,0,4, TimeUnit.SECONDS,"topholders",4);
                    Thread thread = new Thread(topHoldersCrawlJob);
                    thread.start();
                    status=STATUS.RUN;
                }
                break;
            case STOP:
                if(null!=topHoldersCrawlJob){
                    log.info("stop topholders job !");
                    topHoldersCrawlJob.shutDown();
                    topHoldersCrawlJob=null;
                    status=STATUS.STOP;
                }
                break;
        }
    }
}
