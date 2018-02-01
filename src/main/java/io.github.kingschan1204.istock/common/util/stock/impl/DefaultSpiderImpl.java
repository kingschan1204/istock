package io.github.kingschan1204.istock.common.util.stock.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.file.ExcelOperactionTool;
import io.github.kingschan1204.istock.common.util.file.FileCommonOperactionTool;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * 默认爬虫
 *
 * @author chenguoxiang
 * @create 2018-01-31 14:02
 **/
@Component
public class DefaultSpiderImpl implements StockSpider {

    private static Logger log = LoggerFactory.getLogger(DefaultSpiderImpl.class);
    private static final String useAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";

    @Override
    public JSONArray getStockPrice(String[] stockCode) throws Exception {
        StringBuffer queryStr = new StringBuffer();
        for (String code : stockCode) {
            String resultCode = StockSpider.formatStockCode(code);
            if (null != resultCode) {
                queryStr.append(resultCode).append(",");
            }
        }
        String queryCode = queryStr.toString().replaceAll("\\,$", "");
        String query = String.format("http://hq.sinajs.cn/list=%s", queryCode);
        log.info(query);
        String content = Jsoup.connect(query).ignoreContentType(true).get().text();
        String[] line = content.split(";");
        JSONArray rows = new JSONArray();
        JSONObject json;
        for (String s : line) {
            String row = s.trim().replaceAll("^var\\D+|\"", "").replace("=", ",");
            String data[] = row.split(",");
            double xj = Double.parseDouble(data[4]);
            double zs = Double.parseDouble(data[3]);
            double zf = (xj - zs) / zs * 100;
            double today_max = Double.parseDouble(data[5]);
            double today_min = Double.parseDouble(data[6]);
            //log.info(String.format("%s %s 现价:%s 昨收:%s 涨幅:%.2f%s", data[0], data[1], data[3], data[2], zf, "%"));
            NumberFormat nf = NumberFormat.getNumberInstance();
            // 保留两位小数
            nf.setMaximumFractionDigits(2);
            // 如果不需要四舍五入，可以使用RoundingMode.DOWN
            nf.setRoundingMode(RoundingMode.UP);
            json = new JSONObject();
            json.put("code", data[0]);//代码
            json.put("name", data[1]);//名称
            json.put("price", xj);//现价
            json.put("todayMax", today_max);//今日最高价
            json.put("todayMin", today_min);//今日最低价
            json.put("yesterdayPrice", zs);//昨收
            json.put("fluctuate", Double.valueOf(nf.format(zf)));//波动
            json.put("date",new Date());
            rows.add(json);

        }
        return rows;
    }

    @Override
    public JSONObject getStockInfo(String code) throws Exception {
        String url = String.format("http://basic.10jqka.com.cn/%s/", code);
        Document doc = Jsoup.connect(url).get();
        Elements table = doc.getElementsByTag("table");
        //第一个表格的第一行
        Elements tds = table.get(0).select("tr").get(0).select("td");
        String zyyw = tds.get(0).text().replaceAll(".*\\：|\\s*", "");//主营业务
        String sshy = tds.get(1).text().replaceAll(".*\\：|\\s*", "");//所属行业
        Elements tds1 = table.get(1).select("td");
        String dtsyl = tds1.get(0).text().replaceAll(".*\\：|\\s*", "");//市盈率(动态)
        //每股收益： System.out.println(tds1.get(1).select("span").get(0).text() + tds1.get(1).select("span").get(1).text());
        String sjljt = tds1.get(4).text().replaceAll(".*\\：|\\s*", "");//市盈率(静态)
        String sjl = tds1.get(8).text().replaceAll(".*\\：|\\s*", "");//市净率
        String zsz = tds1.get(11).text().replaceAll("\\D+", "");//总市值
        String mgjzc = tds1.get(12).text().replaceAll("\\[.*|", "")
                .replaceAll("[^0-9|\\.]", "");//每股净资产
        String jzcsyl = "-1";
        if (tds1.size() > 14) {
            jzcsyl = tds1.get(14).select("span").get(1).text();//净资产收益率
        }
        JSONObject json = new JSONObject();
        json.put("mainBusiness", zyyw);//主营业务
        json.put("industry", sshy);//所属行业
        json.put("ped", BigDecimal.valueOf(StockSpider.mathFormat(dtsyl)));//市盈率(动态)
        json.put("pes", BigDecimal.valueOf(StockSpider.mathFormat(sjljt)));//市盈率(静态)
        json.put("pb", BigDecimal.valueOf(StockSpider.mathFormat(sjl)));//市净率
        json.put("totalValue", BigDecimal.valueOf(StockSpider.mathFormat(zsz)));//总市值
        json.put("roe", BigDecimal.valueOf(StockSpider.mathFormat(jzcsyl)));//净资产收益率
        json.put("bvps",Double.parseDouble(mgjzc));//每股净资产
        return json;
    }

    @Override
    public JSONArray getHistoryDividendRate(String code) throws Exception {
        String url = String.format("http://basic.10jqka.com.cn/16/%s/bonus.html", code);
        log.info("craw url :{}", url);
        Document doc = Jsoup.connect(url).get();
        Element table = doc.getElementById("bonus_table");
        if (null != table) {
            Elements rows = table.getElementsByTag("tr");
            //报告期	董事会日期	股东大会预案公告日期	实施日期	分红方案说明	A股股权登记日	A股除权除息日	方案进度	股利支付率	分红率
            JSONArray jsons = new JSONArray();
            JSONObject json;
            for (int i = 1; i < rows.size(); i++) {
                String[] data = rows.get(i).select("td").text().split(" ");
                if (data[0].endsWith("年报")) {
                    log.info("报告期:{},A股除权除息日:{},实施日期:{},分红方案说明:{},分红率:{}", data[0], data[6], data[3], data[4], data[9]);
                    //String year, String date, String plan, Double percent
                    double value = -1;
                    if (null != data[9]) {
                        String temp = data[9].replace("%", "");
                        if (temp.matches(regexNumber))
                            value = Double.parseDouble(temp);
                    }
                    //String , String , String plan, Double percent,String executeDate
                    // list.add(new ThsStockDividendRate(, , ,value,data[3]));
                    json = new JSONObject();
                    json.put("year", data[0]);
                    json.put("date", data[6]);
                    json.put("percent", value);
                    json.put("executeDate", data[3]);
                    json.put("remark", data[4]);
                    jsons.add(json);
                }
            }
            return jsons;
        }
        return null;
    }

    @Override
    public JSONArray getHistoryROE(String code) throws Exception {
        String url = String.format("http://basic.10jqka.com.cn/api/stock/export.php?export=main&type=year&code=%s", code);
        String path =String.format("./%s_main_year.xls",code);
        if(!new File(path).exists()){
            //下载
            FileCommonOperactionTool.downloadFile(url, "./", null);
        }
        //读取excel数据
        List<Object[]> list = ExcelOperactionTool.readExcelData(String.format("./%s_main_year.xls", code));
        Object [] year=list.get(1);
        Object [] roe=list.get(10);
        Object [] roeTb=list.get(11);
        JSONArray jsons = new JSONArray();
        JSONObject json;
        for (int i = 1; i <year.length ; i++) {
            json = new JSONObject();
            json.put("year", Integer.valueOf(year[i].toString().replaceAll("\\..*","")));
            json.put("roe", StockSpider.mathFormat(roe[i].toString()));
            json.put("roeTb", StockSpider.mathFormat(roeTb[i].toString()));
            json.put("code", code);
            jsons.add(json);
        }
        return jsons;
    }

    @Override
    public JSONArray getHistoryPE(String code) throws Exception {
        String url = String.format("https://androidinvest.com/Stock/History/%s", StockSpider.formatStockCode(code).toUpperCase());
        log.info("craw history pe :{}", url);
        StockSpider.enableSSLSocket();
        JSONArray jsons = new JSONArray();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent(useAgent).get();
            Element div = doc.getElementById("chart2");
            String data[]=div.text().split("@");//日期@市盈率@股价
            String date[]=data[0].replaceAll("\'|\\[|\\]","").split(",");
            String pe[]=data[1].replaceAll("\'|\\[|\\]","").split(",");
            String price[]=data[2].replaceAll("\'|\\[|\\]","").split(",");
            JSONObject json ;
            for (int i=0;i<date.length;i++) {
                json = new JSONObject();
                json.put("code",code);
                json.put("date", date[i].trim());
                json.put("pe", pe[i].trim());
                json.put("price", price[i].trim());
                jsons.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsons;
    }

    @Override
    public JSONArray getHistoryPB(String code) throws Exception {
        String url = String.format("https://androidinvest.com/Stock/HistoryPB/%s", StockSpider.formatStockCode(code).toUpperCase());
        log.info("craw history pb :{}", url);
        StockSpider.enableSSLSocket();
        JSONArray jsons=new JSONArray();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent(useAgent).get();
            Element div = doc.getElementById("chart4");
            String data[]=div.text().split("@");//日期@市净率@股价
            String date[]=data[0].replaceAll("\'|\\[|\\]","").split(",");
            String pb[]=data[1].replaceAll("\'|\\[|\\]","").split(",");
            String price[]=data[2].replaceAll("\'|\\[|\\]","").split(",");
            JSONObject json ;
            for (int i=0;i<date.length;i++) {
                json = new JSONObject();
                json.put("code",code);
                json.put("date", date[i].trim());
                json.put("pb", pb[i].trim());
                json.put("price", price[i].trim());
                jsons.add(json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsons;
    }
}
