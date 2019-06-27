package io.github.kingschan1204.istock.module.spider.timerjob;

import io.github.kingschan1204.istock.module.spider.timerjob.impl.*;

import java.util.HashMap;

/**
 * 命令定时器简单工厂
 * @author chenguoxiang
 * @create 2019-04-01 16:25
 **/
public class ITimeJobFactory {
    public enum TIMEJOB{
        INDEX,CLEAR,STOCKCODE,INFO,DAILY_BASIC
    }
    private static HashMap<TIMEJOB,ITimerJob> map;

    static {
        map= new HashMap<>();
        map.put(TIMEJOB.INDEX,new IndexTimerJobImpl());
        map.put(TIMEJOB.STOCKCODE,new StockCodeTimerJobImpl());
        map.put(TIMEJOB.CLEAR,new ClearTimerJobImpl());
        map.put(TIMEJOB.INFO,new InfoTimerJobImpl());
        map.put(TIMEJOB.DAILY_BASIC,new DailyBasicTimerJobImpl());
    }

    /**
     * 得到指定的定时器
     * @param timejob
     * @return
     */
    public static ITimerJob getJob(TIMEJOB timejob){
        return map.get(timejob);
    }

}
