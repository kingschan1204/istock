package io.github.kingschan1204.istock.module.spider.timerjob;

import io.github.kingschan1204.istock.module.spider.timerjob.impl.ClearTimerJobImpl;
import io.github.kingschan1204.istock.module.spider.timerjob.impl.IndexTimerJobImpl;
import io.github.kingschan1204.istock.module.spider.timerjob.impl.InfoTimerJobImpl;
import io.github.kingschan1204.istock.module.spider.timerjob.impl.StockCodeTimerJobImpl;

import java.util.HashMap;

/**
 * 命令定时器简单工厂
 * @author chenguoxiang
 * @create 2019-04-01 16:25
 **/
public class ITimeJobFactory {
    public enum TIMEJOB{
        INDEX,CLEAR,STOCKCODE,INFO
    }
    private static HashMap<TIMEJOB,ITimerJob> map;

    static {
        map= new HashMap<>();
        map.put(TIMEJOB.INDEX,new IndexTimerJobImpl());
        map.put(TIMEJOB.STOCKCODE,new StockCodeTimerJobImpl());
        map.put(TIMEJOB.CLEAR,new ClearTimerJobImpl());
        map.put(TIMEJOB.INFO,new InfoTimerJobImpl());
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
