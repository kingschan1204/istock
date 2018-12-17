package io.github.kingschan1204.istock.common.crawl.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.crawl.AbstractSpider;
import io.github.kingschan1204.istock.common.crawl.CrawlResult;
import io.github.kingschan1204.istock.common.exception.CrawlException;
import io.github.kingschan1204.istock.common.http.HttpClientUtils;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.TimeZone;

/**
 * sina price of stock spider
 *
 * @author chenguoxiang
 * @create 2018-11-13 16:56
 **/
@Slf4j
public class SinaStockPriceSpiderImpl implements AbstractSpider {

    private final String sina_url = "http://hq.sinajs.cn/list=%s";

    @Override
    public CrawlResult crawl(String params) throws CrawlException {
        String query = String.format(sina_url, params);
        String result = HttpClientUtils.sendGet(query, null);
        Document doc = Jsoup.parse(result);
        String[] line = doc.text().split(";");
        JSONArray rows = new JSONArray();
        JSONObject json;
        for (String s : line) {
            String row = s.trim().replaceAll("^var\\D+|\"", "").replace("=", ",");
            String data[] = row.split(",");
            if (data.length < 30) {
                throw new CrawlException("代码不存在!");
            }
            double xj = StockSpider.mathFormat(data[4]);
            double zs = StockSpider.mathFormat(data[3]);
            double zf = (xj - zs) / zs * 100;
            double todayMax = StockSpider.mathFormat(data[5]);
            double todayMin = StockSpider.mathFormat(data[6]);
            json = new JSONObject();
            //一般这种是停牌的
            if (xj == 0) {
                //波动
                json.put("fluctuate", 0);
            } else {
                NumberFormat nf = NumberFormat.getNumberInstance();
                // 保留两位小数
                nf.setMaximumFractionDigits(2);
                // 如果不需要四舍五入，可以使用RoundingMode.DOWN
                nf.setRoundingMode(RoundingMode.UP);
                //波动
                json.put("fluctuate", StockSpider.mathFormat(nf.format(zf)));
            }
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
            //代码
            json.put("code", data[0]);
            json.put("type", StockSpider.formatStockCode(data[0]).replaceAll("\\d", ""));
            //名称
            json.put("name", data[1].replaceAll("\\s", ""));
            //现价
            json.put("price", xj);
            //今日最高价
            json.put("todayMax", todayMax);
            //今日最低价
            json.put("todayMin", todayMin);
            //昨收
            json.put("yesterdayPrice", zs);
            json.put("priceDate", StockDateUtil.getCurrentDateTimeNumber());
            rows.add(json);
        }
        System.out.println(rows);
        return null;
    }

    public static void main(String[] args) {
        new SinaStockPriceSpiderImpl().crawl("sh600519,sz000001,sz000568");
    }
}
