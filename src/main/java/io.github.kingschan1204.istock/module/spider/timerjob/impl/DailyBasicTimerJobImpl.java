package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.SimpleTimerJobContainer;
import io.github.kingschan1204.istock.module.spider.crawl.daily.DailyBasicSpider;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 代码info信息更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class DailyBasicTimerJobImpl extends AbstractTimeJob {

    private SimpleTimerJobContainer dailyBasicCrawlJob;

    public DailyBasicTimerJobImpl(){
        name="每日股票指标抓取任务";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if (null == dailyBasicCrawlJob) {
                    log.info("开启basic daily更新线程!");
                    DailyBasicSpider dailyBasicSpider = new DailyBasicSpider();
                    dailyBasicCrawlJob = new SimpleTimerJobContainer(dailyBasicSpider,0,3, TimeUnit.SECONDS,"dailyBasicSpider",4);
                    new Thread(dailyBasicCrawlJob, "DailyBasicCrawlJob").start();
                    status=STATUS.RUN;
                }
                break;
            case STOP:
                if (null != dailyBasicCrawlJob) {
                    log.info("关闭basic daily更新线程!");
                    dailyBasicCrawlJob.shutDown();
                    dailyBasicCrawlJob = null;
                    status=STATUS.STOP;
                }
                break;
        }
    }
}
