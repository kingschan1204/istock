package io.github.kingschan1204.istock.module.spider.crawl.dy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.spider.util.JsoupUitl;
import io.github.kingschan1204.istock.module.spider.util.MathFormat;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

/**
 *
 * @author chenguoxiang
 * @create 2019-07-24 15:07
 **/
@Slf4j
@AllArgsConstructor
public class EastMoneyDividendSpider implements Callable<JSONArray>{

    private String code;
    private String useAgent;
    private Integer timeOut;

    @Override
    public JSONArray call() throws Exception {
        String token = SpringContextUtil.getProperties("eastmoney.token");
        String regex = "T.*";
        String apiUrl = String.format("http://dcfm.eastmoney.com/EM_MutiSvcExpandInterface/api/js/get?type=DCSOBS&token=%s&p=1&ps=50&sr=-1&st=ReportingPeriod&filter=&cmd=%s", token, code);
        String referer = String.format("http://data.eastmoney.com/yjfp/detail/%s.html", code);
        Document doc = null;
        try {
            doc =
            JsoupUitl.getWebPage(apiUrl, Connection.Method.GET,
                    timeOut,useAgent,referer,
                    null, null,
                    true,true).getDocument();
        } catch (Exception e) {
            log.error("分红抓取超时：{} {}", apiUrl,e);
            return new JSONArray();
        }
        JSONArray data = JSONArray.parseArray(doc.text());
        if (null == data) {
            return new JSONArray();
        }
        JSONArray jsons = new JSONArray();
        JSONObject temp;
        for (int i = 0; i < data.size(); i++) {
            JSONObject item = data.getJSONObject(i);
            temp = new JSONObject();
            temp.put("code", code);
            String title = item.getString("ReportingPeriod").replaceAll(regex, "");
            if (title.matches("^\\d{4}\\-12-31$")) {
                title = title.replaceAll("\\-.*", "") + "年报";
            }
            if (title.matches("^\\d{4}\\-06-30$")) {
                title = title.replaceAll("\\-.*", "") + "中报";
            }
            //报告期
            temp.put("title", title);
            //披露时间
            temp.put("releaseDate", item.getString("ResultsbyDate").replaceAll(regex, ""));
            //分配预案
            temp.put("plan", item.getString("AllocationPlan"));
            //送股比例
            temp.put("sgbl", MathFormat.intFormart(item.getString("SGBL")));
            //转股比例
            temp.put("zgbl", MathFormat.intFormart(item.getString("ZGBL")));
            //股息率
            temp.put("percent", MathFormat.doubleFormat(item.getString("GXL"), true));
            //股权登记日
            temp.put("gqdjr", item.getString("GQDJR").replaceAll(regex, ""));
            //除息除权日
            temp.put("cxcqr", item.getString("CQCXR").replaceAll(regex, ""));
            //进度
            temp.put("progress", item.getString("ProjectProgress"));
            //来源
            temp.put("from", "east");
            jsons.add(temp);

        }
        log.info("{}:抓取成功!", code);
        return jsons;
    }
}
