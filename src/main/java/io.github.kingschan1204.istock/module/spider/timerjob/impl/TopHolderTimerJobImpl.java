package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.crawl.topholders.TopHoldersCrawlJob;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

/**
 * 股东更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class TopHolderTimerJobImpl extends AbstractTimeJob {

    private TopHoldersCrawlJob topHoldersCrawlJob;

    public TopHolderTimerJobImpl(){
        name="前10大股东抓取任务";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if(null==topHoldersCrawlJob){
                    log.info("start topholders job...");
                    topHoldersCrawlJob=new TopHoldersCrawlJob();
                    Thread thread = new Thread(topHoldersCrawlJob);
                    thread.start();
                    status=STATUS.RUN;
                }
                break;
            case STOP:
                if(null!=topHoldersCrawlJob){
                    log.info("stop topholders job !");
                    topHoldersCrawlJob.stopTask();
                    topHoldersCrawlJob=null;
                    status=STATUS.STOP;
                }
                break;
        }
    }
}
