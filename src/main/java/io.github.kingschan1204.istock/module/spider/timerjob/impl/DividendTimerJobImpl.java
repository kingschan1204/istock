package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.crawl.dy.DividendCrawlJob;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码info信息更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class DividendTimerJobImpl extends AbstractTimeJob {

    private DividendCrawlJob dividendCrawlJob;

    public DividendTimerJobImpl(){
        name="历史分红抓取任务";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if (null == dividendCrawlJob) {
                    log.info("开启dividendCrawlJob更新线程!");
                    dividendCrawlJob = new DividendCrawlJob();
                    new Thread(dividendCrawlJob, "dividendCrawlJob").start();
                    status=STATUS.RUN;
                }
                break;
            case STOP:
                if (null != dividendCrawlJob) {
                    log.info("关闭dividendCrawlJob更新线程!");
                    dividendCrawlJob.stopTask();
                    dividendCrawlJob = null;
                    status=STATUS.STOP;
                }
                break;
        }
    }
}
