package io.github.kingschan1204.istock.common.util.stock.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author chenguoxiang
 * @create 2018-04-12 14:37
 **/
@RefreshScope
@Component("EastmoneySpider")
public class EastmoneySpider extends DefaultSpiderImpl {

    @Value("${eastmoney.token}")
    private String token ;

    /**
     * ReportingPeriod:报告期
     * ResultsbyDate：披露时间
     * SGBL：送股比例
     * ZGBL：转股比例
     * GXL：股息率
     * TotalEquity：总股本
     * AllocationPlan：分配预案
     * ProjectProgress:方案进度
     * GQDJR：股权登记日
     * CQCXR：除息除权日
     *
     * @param code
     * @return
     * @throws Exception
     */
    @Override
    public JSONArray getHistoryDividendRate(String code) throws Exception {
        String regex = "T.*";
        String apiUrl = String.format("http://dcfm.eastmoney.com/EM_MutiSvcExpandInterface/api/js/get?type=DCSOBS&token=%s&p=1&ps=50&sr=-1&st=ReportingPeriod&filter=&cmd=%s", token, code);
        String referer = String.format("http://data.eastmoney.com/yjfp/detail/%s.html", code);
        Document doc = Jsoup.connect(apiUrl).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3346.9 Safari/537.36")
                .timeout(10000)
                .ignoreContentType(true)
                .referrer(referer).get();
        JSONArray data = JSONArray.parseArray(doc.text());
        if(null==data)return new JSONArray();

        JSONArray jsons = new JSONArray();
        JSONObject temp;
        for (int i = 0; i < data.size(); i++) {
            JSONObject item = data.getJSONObject(i);
            temp = new JSONObject();
            temp.put("code", code);
            temp.put("title", item.getString("ReportingPeriod").replaceAll(regex, ""));//报告期
            temp.put("releaseDate", item.getString("ResultsbyDate").replaceAll(regex, ""));//披露时间
            temp.put("plan", item.getString("AllocationPlan"));//分配预案
            temp.put("sgbl", intFormart(item.getString("SGBL")));//送股比例
            temp.put("zgbl", intFormart(item.getString("ZGBL")));//转股比例
            temp.put("percent", doubleFormat(item.getString("GXL"))* 100);//股息率
            temp.put("gqdjr", item.getString("GQDJR").replaceAll(regex, ""));//股权登记日
            temp.put("cxcqr", item.getString("CQCXR").replaceAll(regex, ""));//除息除权日
            temp.put("progress", item.getString("ProjectProgress"));//进度
            jsons.add(temp);

        }
        return jsons;
    }

    public int intFormart(String data) {
        if (data.matches("\\d+")) {
            return Integer.valueOf(data);
        }
        return 0;
    }

    /**
     * 4舍5入 保留两位小数
     *
     * @param math
     * @return
     */
    public double doubleFormat(String math) {
        String regex_number="^[-+]?([0]{1}(\\.[0-9]+)?|[1-9]{1}\\d*(\\.[0-9]+)?)";
        if(math.matches(regex_number)){
            Double d = Double.parseDouble(math);
            BigDecimal b = new BigDecimal(d);
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
       return 0d;
    }



}
