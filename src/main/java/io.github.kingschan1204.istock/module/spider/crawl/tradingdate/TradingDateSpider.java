package io.github.kingschan1204.istock.module.spider.crawl.tradingdate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import io.github.kingschan1204.istock.module.spider.util.JsoupUitl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * 交易日列表爬虫
 * @author chenguoxiang
 * @create 2019-03-26 17:57
 *  http://www.szse.cn/api/report/exchange/onepersistentday/monthList?random=0.2812774184280482
 **/
@Slf4j
public class TradingDateSpider implements Callable<Map<String,Boolean>>{
    private final String baseUrl="http://www.szse.cn/api/report/exchange/onepersistentday/monthList";
    private String month;
    private String url;
    public TradingDateSpider(String month){
        this.month=month;
        url=null==month?baseUrl:String.format("%s?month=%s",baseUrl,month);
    }

    @Override
    public Map<String, Boolean> call() throws Exception {
        WebPage webPage= JsoupUitl.getWebPage(url, Connection.Method.GET,
                4000,null,"http://www.szse.cn/disclosure/index.html");
        Optional.ofNullable(webPage).map(webPage1 -> webPage1.getDocument().text()).orElseThrow(()->new Exception("web page null"));
        JSONObject json = JSON.parseObject(webPage.getDocument().text());
        JSONArray jsonArray = json.getJSONArray("data");
        Map<String, Boolean> data = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            data.put(
                    jsonArray.getJSONObject(i).getString("jyrq"),
                    jsonArray.getJSONObject(i).getInteger("jybz")==1
            );
        }
        return data;
    }
}
