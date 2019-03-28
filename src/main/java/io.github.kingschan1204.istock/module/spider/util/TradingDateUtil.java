package io.github.kingschan1204.istock.module.spider.util;

import io.github.kingschan1204.istock.common.util.cache.EhcacheUtil;
import io.github.kingschan1204.istock.module.spider.crawl.tradingdate.TradingDateSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * 交易时间工具类
 * @author chenguoxiang
 * @create 2019-03-28 12:57
 **/
@Slf4j
@Component
public class TradingDateUtil {

    private final String CACHE_NAME="TradingDate";
    @Autowired
    private EhcacheUtil ehcacheUtil;

    /**
     * 是否为交易日
     * @param date  日期例如：2019-03-28
     * @return
     */
    public  boolean isTradingDay(String date)throws Exception{
        if(!date.matches("\\d{4}\\-\\d{2}\\-\\d{2}")){
            throw new Exception("传入日期格式不正确：yyyy-MM-dd");
        }
        if(ehcacheUtil.contrainKey(CACHE_NAME,date)){
            log.info("缓存{}存在key:{}直接查找",CACHE_NAME,date);
            return (boolean)ehcacheUtil.getKey(CACHE_NAME,date);
        }else{
            String month=date.substring(0,7);
            TradingDateSpider spider = new TradingDateSpider(month);
            FutureTask<Map<String, Boolean>> futureTask = new FutureTask<Map<String, Boolean>>(spider);
            new Thread(futureTask).start();
            Map<String, Boolean> map= futureTask.get();
             map.keySet().stream().forEach(key->{
                 ehcacheUtil.addKey(CACHE_NAME,key,map.get(key));
             });
             return map.get(date);
        }
    }

    /**
     * 是否为交易日间
     * @param hour
     * @param minute
     * @return
     */
    public  boolean isTradingTime(int hour,int minute){
        return false;
    }



}
