package io.github.kingschan1204.istock.module.spider.crawl.tradingdate;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 交易日列表爬虫
 * @author chenguoxiang
 * @create 2019-03-26 17:57
 *  http://www.szse.cn/api/report/exchange/onepersistentday/monthList?random=0.2812774184280482
 **/
public class TradingDateSpider implements Callable<Map<String,Boolean>>{
    @Override
    public Map<String, Boolean> call() throws Exception {
        return null;
    }
}
