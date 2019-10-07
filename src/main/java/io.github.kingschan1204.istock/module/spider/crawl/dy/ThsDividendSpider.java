package io.github.kingschan1204.istock.module.spider.crawl.dy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.module.spider.util.JsoupUitl;
import io.github.kingschan1204.istock.module.spider.util.MathFormat;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.Callable;

/**
 *
 * @author chenguoxiang
 * @create 2019-07-24 15:07
 **/
@Slf4j
@AllArgsConstructor
public class ThsDividendSpider implements Callable<JSONArray>{

    private String code;
    private String useAgent;
    private Integer timeOut;

    @Override
    public JSONArray call() throws Exception {
        String stockCode = code.replaceAll("\\D", "");
        String url = String.format("http://basic.10jqka.com.cn/16/%s/bonus.html", stockCode);
        log.info("craw url :{}", url);
        Document doc = JsoupUitl.getWebPage(url, Connection.Method.GET,timeOut,useAgent,null).getDocument();
        Element table = doc.getElementById("bonus_table");
        if (null != table) {
            Elements rows = table.getElementsByTag("tr");
            //报告期	董事会日期	股东大会预案公告日期	实施公告日	分红方案说明	A股股权登记日	A股除权除息日	分红总额	方案进度	股利支付率	税前分红率
            JSONArray jsons = new JSONArray();
            JSONObject json;
            for (int i = 1; i < rows.size(); i++) {
                String rowtext = rows.get(i).select("td").text();
                String[] data = rowtext.split(" ");
                if ("--".equals(data[6]) || "--".equals(data[9])) {
                    continue;
                }
                log.debug("报告期:{},A股除权除息日:{},实施日期:{},分红方案说明:{},分红率:{}", data[0], data[6], data[3], data[4], data[10]);
                json = new JSONObject();
                json.put("code", stockCode);
                //报告期
                json.put("title", data[0]);
                //披露时间  董事会日期
                json.put("releaseDate", data[1]);
                //分红方案
                json.put("plan", data[4]);
                //送股比例
                json.put("sgbl", 0);
                //转股比例
                json.put("zgbl", 0);
                //分红率
                json.put("percent", MathFormat.doubleFormat(data[10]));
                //股权登记日
                json.put("gqdjr", data[5]);
                //除息除权日
                json.put("cxcqr", data[6]);
                //方案进度
                json.put("progress", data[8]);
                //来源
                json.put("from", "ths");
                jsons.add(json);
            }
            return jsons;
        }
        return null;
    }
}
