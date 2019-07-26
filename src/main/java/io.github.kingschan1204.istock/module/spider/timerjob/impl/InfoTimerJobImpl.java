package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.crawl.info.InfoCrawlJob;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码info信息更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class InfoTimerJobImpl extends AbstractTimeJob {

    private InfoCrawlJob infoCrawlJob;

    public InfoTimerJobImpl(){
        name="股票详情数据抓取任务";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if (null == infoCrawlJob) {
                    log.info("开启info更新线程!");
                    infoCrawlJob = new InfoCrawlJob();
                    new Thread(infoCrawlJob, "InfoCrawlJob").start();
                    status=STATUS.RUN;
                }
                break;
            case STOP:
                if (null != infoCrawlJob) {
                    log.info("关闭thsinfo更新线程!");
                    infoCrawlJob.stopTask();
                    infoCrawlJob = null;
                    status=STATUS.STOP;
                }
                break;
        }
    }
}
