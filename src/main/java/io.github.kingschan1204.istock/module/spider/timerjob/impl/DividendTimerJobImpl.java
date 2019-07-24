package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.crawl.dy.DividendCrawlJob;
import io.github.kingschan1204.istock.module.spider.crawl.info.InfoCrawlJob;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码info信息更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class DividendTimerJobImpl implements ITimerJob{

    private DividendCrawlJob dividendCrawlJob;

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if (null == dividendCrawlJob) {
                    log.info("开启dividendCrawlJob更新线程!");
                    dividendCrawlJob = new DividendCrawlJob();
                    new Thread(dividendCrawlJob, "dividendCrawlJob").start();
                }
                break;
            case STOP:
                if (null != dividendCrawlJob) {
                    log.info("关闭dividendCrawlJob更新线程!");
                    dividendCrawlJob.stopTask();
                    dividendCrawlJob = null;
                }
                break;
        }
    }
}
