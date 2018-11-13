package io.github.kingschan1204.istock.common.util.stock.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.file.ExcelOperactionTool;
import io.github.kingschan1204.istock.common.util.file.FileCommonOperactionTool;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.RoundingMode;
import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.*;

/**
 * 默认爬虫
 * @author chenguoxiang
 * @create 2018-01-31 14:02
 **/
@Slf4j
@RefreshScope
@Primary
@Component("DefaultSpiderImpl")
public class DefaultSpiderImpl implements StockSpider {

    @Value("${spider.timeout}")
    /**
     * 8s超时
     */
    protected int timeout;
    @Value("${spider.useagent}")
    protected String useAgent;
    @Value("${xueqiu.token}")
    protected String xueQiuToken;

    @Override
    public JSONArray getStockPrice(String[] stockCode) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        for (String code : stockCode) {
            String resultCode = StockSpider.formatStockCode(code);
            if (null != resultCode) {
                queryStr.append(resultCode).append(",");
            }
        }
        String queryCode = queryStr.toString().replaceAll("\\,$", "");
        String query = String.format("http://hq.sinajs.cn/list=%s", queryCode);
        log.info(query);
        String content = null;
        try {
            content = Jsoup.connect(query).timeout(timeout).ignoreContentType(true).get().text();
        } catch (SocketTimeoutException e) {
            log.error("超时{}", query);
            return null;
        }
        log.debug(content);
        String[] line = content.split(";");
        JSONArray rows = new JSONArray();
        JSONObject json;
        for (String s : line) {
            String row = s.trim().replaceAll("^var\\D+|\"", "").replace("=", ",");
            String data[] = row.split(",");
            if (data.length < 30) {
                throw new Exception("代码不存在!");
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
        return rows;
    }

    @Override
    public JSONObject getStockInfo(String code) throws Exception {
        String stockCode = code.replaceAll("\\D", "");
        String regex = ".*\\：|\\s*";
        String url = String.format("http://basic.10jqka.com.cn/%s/", stockCode);
        Document doc = Jsoup.connect(url).userAgent(useAgent).timeout(timeout).get();
        Elements table = doc.getElementsByTag("table");
        //第一个表格的第一行
        Elements tds = table.get(0).select("tr").get(0).select("td");
        //主营业务
        String zyyw = tds.get(0).text().replaceAll(regex, "");
        //所属行业
        String sshy = tds.get(1).text().replaceAll(regex, "");
        Elements tds1 = table.get(1).select("td");
        //市盈率(动态)
        String dtsyl = tds1.get(0).text().replaceAll(regex, "");
        //市盈率(静态)
        String sjljt = tds1.get(4).text().replaceAll(regex, "");
        //市净率
        String sjl = tds1.get(8).text().replaceAll(regex, "");
        //总市值
        String zsz = tds1.get(11).text().replaceAll("\\D+", "");
        //每股净资产
        double mgjzc = StockSpider.mathFormat(tds1.get(12).text().replaceAll("\\[.*|", ""));
        String jzcsyl = "-1";
        if (tds1.size() > 14) {
            //净资产收益率
            jzcsyl = tds1.get(14).select("span").get(1).text();
        }
        JSONObject json = new JSONObject();
        //主营业务
        json.put("mainBusiness", zyyw);
        //所属行业
        json.put("industry", sshy);
        //市盈率(动态)
        json.put("ped", StockSpider.mathFormat(dtsyl));
        //市盈率(静态)
        json.put("pes", StockSpider.mathFormat(sjljt));
        //市净率
        json.put("pb", StockSpider.mathFormat(sjl));
        //总市值
        json.put("totalValue", StockSpider.mathFormat(zsz));
        //净资产收益率
        json.put("roe", StockSpider.mathFormat(jzcsyl));
        //每股净资产
        json.put("bvps", mgjzc);
        json.put("infoDate", StockDateUtil.getCurrentDateNumber());
        json.put("code", stockCode);
        json.put("type", StockSpider.formatStockCode(stockCode).replaceAll("\\d", ""));
        return json;
    }

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
                json.put("percent", StockSpider.mathFormat(data[9]));
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
        //净资产收益率
        Object[] roe = list.get(10);
        //净资产收益率-摊薄
        Object[] roeTb = list.get(11);
        JSONArray jsons = new JSONArray();
        JSONObject json;
        for (int i = 1; i < year.length; i++) {
            json = new JSONObject();
            json.put("year", Integer.valueOf(year[i].toString().replaceAll("\\..*", "")));
            json.put("roe", StockSpider.mathFormat(roe[i].toString()));
            json.put("roeTb", StockSpider.mathFormat(roeTb[i].toString()));
            json.put("code", code);
            json.put("date", new Date());
            jsons.add(json);
        }
        return jsons;
    }


    @Override
    public List<String> getAllStockCode() throws Exception {
        String url = "https://touzi.sina.com.cn/api/openapi.php/TzyFreeService.searchStocks";
        log.info("craw all stock code :{}", url);
        StockSpider.enableSSLSocket();
        Document doc = null;
        List<String> codes = null;
        try {
            doc = Jsoup.connect(url).userAgent(useAgent).timeout(timeout).ignoreContentType(true).get();
            String text = doc.text();
            JSONObject json = JSON.parseObject(text);
            JSONArray value = json.getJSONObject("result").getJSONObject("data").getJSONArray("stocks");
            codes = value.toJavaList(String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return codes;
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
        String filename = String.format("sz_code_%s.xlsx", StockDateUtil.getCurrentDateNumber());
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
