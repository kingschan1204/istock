package io.github.kingschan1204.istock.common.util.stock.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.file.ExcelOperactionTool;
import io.github.kingschan1204.istock.common.util.file.FileCommonOperactionTool;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.spider.util.MathFormat;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 默认爬虫
 * @author chenguoxiang
 * @create 2018-01-31 14:02
 **/
@Slf4j
@Primary
@Component("DefaultSpiderImpl")
public class DefaultSpiderImpl implements StockSpider {

    /**
     * 8s超时
     */
    @Value("${spider.timeout}")
    protected int timeout;
    @Value("${spider.useagent}")
    protected String useAgent;
    @Value("${xueqiu.token}")
    protected String xueQiuToken;



    @Override
    public JSONArray getHistoryDividendRate(String code) throws Exception {
        String stockCode = code.replaceAll("\\D", "");
        String url = String.format("http://basic.10jqka.com.cn/16/%s/bonus.html", stockCode);
        log.info("craw url :{}", url);
        Document doc = Jsoup.connect(url).timeout(timeout).userAgent(useAgent).get();
        Element table = doc.getElementById("bonus_table");
        if (null != table) {
            Elements rows = table.getElementsByTag("tr");
            //报告期	董事会日期	股东大会预案公告日期	实施日期	分红方案说明	A股股权登记日	A股除权除息日	方案进度	股利支付率	分红率
            JSONArray jsons = new JSONArray();
            JSONObject json;
            for (int i = 1; i < rows.size(); i++) {
                String rowtext = rows.get(i).select("td").text();
                String[] data = rowtext.split(" ");
                if ("--".equals(data[6]) || "--".equals(data[9])) {
                    continue;
                }
                log.debug("报告期:{},A股除权除息日:{},实施日期:{},分红方案说明:{},分红率:{}", data[0], data[6], data[3], data[4], data[9]);
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
                json.put("percent", MathFormat.doubleFormat(data[9]));
                //股权登记日
                json.put("gqdjr", data[5]);
                //除息除权日
                json.put("cxcqr", data[6]);
                //方案进度
                json.put("progress", data[7]);
                //来源
                json.put("from", "ths");
                jsons.add(json);
            }
            return jsons;
        }
        return null;
    }

    @Override
    public JSONArray getHistoryROE(String code) throws Exception {
        String url = String.format("http://basic.10jqka.com.cn/api/stock/export.php?export=main&type=year&code=%s", code);
        String path = String.format("./data/%s.xls", code);
        String referrer=String.format("http://basic.10jqka.com.cn/%s/finance.html",code);
        if (!new File(path).exists()) {
            //下载
            path= FileCommonOperactionTool.downloadFile(url, referrer,"./data/", code+".xls");
        }else{
            log.info("文件存在，直接读取：{}",path);
        }
        //读取excel数据
        List<Object[]> list = ExcelOperactionTool.readExcelData(path);
        //报告期 年
        Object[] year = list.get(1);
        //净利润
        Object[] profits = list.get(3);
        //净利润增长率
        Object[] profits_percent = list.get(4);
        //营业总收入
        Object[] operating_income = list.get(7);
        //营业总收入同比增长率
        Object[] income_percent = list.get(8);
        //每股净资产
        Object[] net_assets = list.get(9);
        //净资产收益率
        Object[] roe = list.get(10);
        //净资产收益率-摊薄
        Object[] roeTb = list.get(11);
        //资产负债比率
        Object[] asset_liability = list.get(12);
        JSONArray jsons = new JSONArray();
        JSONObject json;
        //基数压缩数字以亿为单位
        int base =100000000;
        for (int i = 1; i < year.length; i++) {
            json = new JSONObject();
            json.put("year", Integer.valueOf(year[i].toString().replaceAll("\\..*", "")));
            json.put("roe", MathFormat.doubleFormat(roe[i].toString()));
            json.put("roeTb", MathFormat.doubleFormat(roeTb[i].toString()));
            json.put("code", code);
            json.put("date", new Date());
            json.put("profits",MathFormat.doubleFormat(profits[i].toString(),base,true));
            json.put("profits_percent",MathFormat.doubleFormat(profits_percent[i].toString()));
            json.put("operating_income",MathFormat.doubleFormat(operating_income[i].toString(),base,true));
            json.put("income_percent",MathFormat.doubleFormat(income_percent[i].toString()));
            json.put("net_assets",MathFormat.doubleFormat(net_assets[i].toString()));
            json.put("asset_liability",MathFormat.doubleFormat(asset_liability[i].toString()));
            jsons.add(json);
        }
        return jsons;
    }




    /**
     * 得到股票实时股息
     *
     * @param page
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject getDy(int page) throws Exception {
        String url = "https://xueqiu.com/stock/screener/screen.json?category=SH&exchange=&areacode=&indcode=&orderby=symbol&order=desc&current=ALL&pct=ALL&page=%s&dy=0_19.31&size=100";
        url = String.format(url, page);
        log.info("更新dy第{}页", page);
        StockSpider.enableSSLSocket();
        Document infoDoc = Jsoup.connect(url).userAgent(useAgent).referrer("https://xueqiu.com/hq/screener")
                .timeout(timeout)
                .cookie("xq_a_token", xueQiuToken)
                .ignoreContentType(true)
                .get();
        JSONObject json = JSON.parseObject(infoDoc.text());
        JSONArray jsons = json.getJSONArray("list");
        if (null != jsons) {
            JSONArray items = new JSONArray();
            for (int i = 0; i < jsons.size(); i++) {
                JSONObject item = new JSONObject();
                item.put("code", jsons.getJSONObject(i).getString("symbol").replaceAll("\\D", ""));
                item.put("dy", jsons.getJSONObject(i).getDoubleValue("dy"));
                items.add(item);
            }
            json.put("list", items);
            return json;
        }
        return null;
    }

    @Override
    public List<String> getStockCodeBySH() throws Exception {
        List<String> list = new ArrayList<>();
        String url = "http://www.sse.com.cn/js/common/ssesuggestdata.js";
        log.info("craw sh codes :{}", url);
        Document infoDoc = Jsoup.connect(url).userAgent(useAgent)
                .timeout(timeout)
                .ignoreContentType(true)
                .get();
        String result = StockSpider.findStrByRegx(infoDoc.html(), "60\\d{4}");
        String[] codes = result.split(",");
        Arrays.stream(codes).forEach(code ->{
            list.add(StockSpider.formatStockCode(code));
        });
        return list;
    }

    @Override
    public List<String> getStockCodeBySZ() throws Exception {
        List<String> codes = new ArrayList<>();
        String url = "http://www.szse.cn/szseWeb/ShowReport.szse?SHOWTYPE=xlsx&CATALOGID=1110&tab2PAGENO=1&ENCODE=1&TABKEY=tab2";
        log.info("craw sz codes :{}", url);
        String filename = String.format("sz_code_%s.xlsx", TradingDateUtil.getDateYYYYMMdd());
        String path = String.format("./data/%s", filename);
        if (!new File(path).exists()) {
            //下载
            FileCommonOperactionTool.downloadFile(url, "","./data/", filename);
        }
        //读取excel数据
        List<Object[]> list = ExcelOperactionTool.readExcelData(path);
        for (Object[] row : list) {
            if (row[0].toString().trim().matches("^00\\d{4}")) {
                codes.add(StockSpider.formatStockCode(row[0].toString().trim()));
            }
        }
        return codes;
    }



}
