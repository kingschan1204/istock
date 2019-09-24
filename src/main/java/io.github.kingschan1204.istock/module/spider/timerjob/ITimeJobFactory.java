package io.github.kingschan1204.istock.module.spider.timerjob;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.module.spider.timerjob.impl.*;

import java.util.HashMap;

/**
 * 命令定时器简单工厂
 * @author chenguoxiang
 * @create 2019-04-01 16:25
 **/
public class ITimeJobFactory {
    public enum TIMEJOB{
        CORE_SCHEDULE,INDEX,CLEAR,STOCKCODE,INFO,DAILY_BASIC,TOP_HOLDER,DY,YEAR_REPORT,DIVIDEND,DYROE
    }
    private static HashMap<TIMEJOB,ITimerJob> map;

    static {
        map= new HashMap<>();
        map.put(TIMEJOB.CORE_SCHEDULE,new CoreScheduleTimerJobImpl());
        map.put(TIMEJOB.INDEX,new IndexTimerJobImpl());
        map.put(TIMEJOB.STOCKCODE,new StockCodeTimerJobImpl());
        map.put(TIMEJOB.CLEAR,new ClearTimerJobImpl());
        map.put(TIMEJOB.INFO,new InfoTimerJobImpl());
        map.put(TIMEJOB.DAILY_BASIC,new DailyBasicTimerJobImpl());
        map.put(TIMEJOB.TOP_HOLDER,new TopHolderTimerJobImpl());
        map.put(TIMEJOB.DY,new XueQiuDyTimerJobImpl());
        map.put(TIMEJOB.YEAR_REPORT,new YearReportTimerJobImpl());
        map.put(TIMEJOB.DIVIDEND,new DividendTimerJobImpl());
        map.put(TIMEJOB.DYROE,new DyRoeAnalysisJobImpl());




    }

    /**
     * 得到指定的定时器
     * @param timejob
     * @return
     */
    public static ITimerJob getJob(TIMEJOB timejob){
        return map.get(timejob);
    }

    public static JSONArray getTasks(){
        JSONArray jsonArray = new JSONArray();
        JSONObject rows;
        for (TIMEJOB key :map.keySet()) {
            rows=new JSONObject();
            AbstractTimeJob job = (AbstractTimeJob) map.get(key);
            rows.put("id",key);
            rows.put("name",job.name);
            rows.put("status",job.status);
            jsonArray.add(rows);
        }
        return jsonArray;
    }

}
