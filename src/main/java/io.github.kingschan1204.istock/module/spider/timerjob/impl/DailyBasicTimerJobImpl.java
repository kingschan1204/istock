package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.crawl.daily.DailyBasicCrawlJob;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码info信息更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class DailyBasicTimerJobImpl extends AbstractTimeJob {

    private DailyBasicCrawlJob dailyBasicCrawlJob;

    public DailyBasicTimerJobImpl(){
        name="每日股票指标抓取任务";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if (null == dailyBasicCrawlJob) {
                    log.info("开启basic daily更新线程!");
                    dailyBasicCrawlJob = new DailyBasicCrawlJob();
                    new Thread(dailyBasicCrawlJob, "DailyBasicCrawlJob").start();
                    status=STATUS.RUN;
                }
                break;
            case STOP:
                if (null != dailyBasicCrawlJob) {
                    log.info("关闭basic daily更新线程!");
                    dailyBasicCrawlJob.stopTask();
                    dailyBasicCrawlJob = null;
                    status=STATUS.STOP;
                }
                break;
        }
    }
}
