package io.github.kingschan1204.istock.module.spider.openapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import io.github.kingschan1204.istock.module.spider.util.JsoupUitl;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chenguoxiang
 * @create 2019-11-13 11:21
 **/
@Slf4j
@Component
public class XueQiuApi {
    @Value("${xueqiu.token}")
    private String token;
    //股东人数变化
    private String holders_url="https://stock.xueqiu.com/v5/stock/f10/cn/holders.json?symbol=%s&extend=true&page=1&size=40";
    private Map<String, String> cookie;
    @Autowired
    private TradingDateUtil tradingDateUtil;

    @PostConstruct
    private void init(){
        this.cookie = new HashMap<>();
        this.cookie.put("xq_a_token", token);
    }

    /**
     * 得到股东人数变化
     * @param code 代码
     * @return
     */
    public JSONArray getHolders(String code){
        String url = String.format(holders_url, StockSpider.formatStockCode(code).toUpperCase());
        WebPage webPage =
                JsoupUitl.getWebPage(
                        url, Connection.Method.GET,
                        8000, null, "https://xueqiu.com/",
                        cookie, null,
                        true, true);
        JSONObject data = JSONObject.parseObject(webPage.getDocument().text());
        if(data.getInteger("error_code")==0){
            JSONArray rows = data.getJSONObject("data").getJSONArray("items");
            for (int i = 0; i <rows.size() ; i++) {
                String date=tradingDateUtil.dateFormat(rows.getJSONObject(i).getLong("timestamp"));
                rows.getJSONObject(i).put("timestamp",date);
            }
            return rows;
        }
        log.error("请求错误：{}",data.getString("error_description"));
        return null;
    }


}
