package io.github.kingschan1204.istock.module.spider.schedule;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * 调度作业
 *
 * @author chenguoxiang
 * @create 2019-03-26 17:00
 **/
@Slf4j
public class ScheduleThread implements Runnable {
    //开市日期字典
    private Map<String,Boolean> openMarketDate;
    //开市字典上次刷新时间
    private LocalDate refreshDate;

    @Override
    public void run() {
         LocalDate date = LocalDate.now();
         LocalTime time = LocalTime.now();
        //当前时间 年-月-日 时 分 秒
        System.out.printf("%d-%d-%d %d:%d:%d",date.getYear(), date.getMonthValue(), date.getDayOfMonth(),time.getHour(), time.getMinute(), time.getSecond());
        System.out.println();
        //今天是否开市
        //是否是 9：30 - 11：30 || 1：00 - 3：00

    }


}
