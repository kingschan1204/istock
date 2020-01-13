package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.module.spider.SimpleTimerJobContainer;
import io.github.kingschan1204.istock.module.spider.crawl.info.ThsInfoSpider;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 代码info信息更新命令封装
 * @author chenguoxiang
 * @create 2019-04-01 16:21
 **/
@Slf4j
public class FundHoldersTimerJobImpl extends AbstractTimeJob {

    private SimpleTimerJobContainer infoCrawlJob;

    public FundHoldersTimerJobImpl(){
        name="基金持仓任务";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        switch (command){
            case START:
                if (null == infoCrawlJob) {
                    log.info("开启info更新线程!");
                    ThsInfoSpider infoSpider = new ThsInfoSpider();
                    infoCrawlJob = new SimpleTimerJobContainer(infoSpider,0,1, TimeUnit.SECONDS,"ths-info",4);
                    new Thread(infoCrawlJob, "InfoCrawlJob").start();
                    status=STATUS.RUN;
                }
                break;
            case STOP:
                if (null != infoCrawlJob) {
                    log.info("关闭thsinfo更新线程!");
                    infoCrawlJob.shutDown();
                    infoCrawlJob = null;
                    status=STATUS.STOP;
                }
                break;
        }
    }
}
