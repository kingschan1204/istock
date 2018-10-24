package io.github.kingschan1204.istock.common.util.stock.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import org.jsoup.Jsoup;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 用腾讯的接口
 * @author chenguoxiang
 * @create 2018-10-24 9:31
 **/
@RefreshScope
@Component("TencentSpider")
public class TencentSpider extends DefaultSpiderImpl{

    @Override
    public JSONArray getStockPrice(String[] stockCode) throws Exception {
        String codes= Arrays.stream(stockCode).collect(Collectors.joining(","));
        String api =String.format("http://qt.gtimg.cn/q=%s",codes);
        String content = null;
        try {
            content = Jsoup.connect(api).timeout(timeout).ignoreContentType(true).get().text();
            List<String> rows =Arrays.asList(content.split(";"));
            JSONArray jsonArray = new JSONArray();
            JSONObject json;
            for (int i = 0; i < rows.size(); i++) {
                String [] item =rows.get(i).replaceAll("v_.*=|\"","").split("~");
                json= new JSONObject();
                //代码
                json.put("code", item[2]);
                //波动
                json.put("fluctuate", item[32]);
                json.put("type", StockSpider.formatStockCode(item[2]).replaceAll("\\d", ""));
                //名称
                json.put("name", item[1].replaceAll("\\s", ""));
                //现价
                json.put("price", item[3]);
                //今日最高价
                json.put("todayMax", item[41]);
                //今日最低价
                json.put("todayMin", item[42]);
                //昨收
                json.put("yesterdayPrice", item[4]);
                json.put("priceDate", StockDateUtil.getCurrentDateTimeNumber());
                jsonArray.add(json);
            }
            return jsonArray;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }


}
