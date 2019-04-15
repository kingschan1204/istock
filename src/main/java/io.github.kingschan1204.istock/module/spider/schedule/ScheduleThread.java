package io.github.kingschan1204.istock.module.spider.schedule;

import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.spider.crawl.info.InfoCrawlJob;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimeJobFactory;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 调度作业
 *
 * @author chenguoxiang
 * @create 2019-03-26 17:00
 **/
@Slf4j
public class ScheduleThread implements Runnable {




    void jobProcess() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        TradingDateUtil tradingDateUtil = SpringContextUtil.getBean(TradingDateUtil.class);

        if (tradingDateUtil.isTradingTimeNow()) {
            ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.INDEX).execute(ITimerJob.COMMAND.START);
        } else {
            ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.INDEX).execute(ITimerJob.COMMAND.STOP);
        }

        switch (dateTime.getHour()) {
            case 0:
                //晚上12点
                if(dateTime.getMinute()==1){
                    ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.CLEAR).execute(null);
                    ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.STOCKCODE).execute(null);
                }
                break;
            case 9:
                //早上9点
                break;
            case 11:
                //上午11点
                break;
            case 13:
                //下午1点
                break;
            case 15:
                //下午3点  闭市后爬取info信息
                ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.INFO).execute(ITimerJob.COMMAND.START);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        try {
            jobProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(dateTime.getHour());
    }

}
