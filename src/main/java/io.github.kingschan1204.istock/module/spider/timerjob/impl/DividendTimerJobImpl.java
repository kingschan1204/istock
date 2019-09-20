package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.SimpleTimerJobContainer;
import io.github.kingschan1204.istock.module.spider.crawl.dy.DividendSpider;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 代码info信息更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class DividendTimerJobImpl extends AbstractTimeJob {

    private SimpleTimerJobContainer dividendCrawlJob;

    public DividendTimerJobImpl(){
        name="历史分红抓取任务";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if (null == dividendCrawlJob) {
                    log.info("开启dividendCrawlJob更新线程!");
                    DividendSpider dividendSpider = new DividendSpider();
                    dividendCrawlJob = new SimpleTimerJobContainer(dividendSpider,0,6, TimeUnit.SECONDS,"dividend",4);
                    new Thread(dividendCrawlJob, "dividendCrawlJob").start();
                    status=STATUS.RUN;
                }
                break;
            case STOP:
                if (null != dividendCrawlJob) {
                    log.info("关闭dividendCrawlJob更新线程!");
                    dividendCrawlJob.shutDown();
                    dividendCrawlJob = null;
                    status=STATUS.STOP;
                }
                break;
        }
    }
}
