package io.github.kingschan1204.istock.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kingschan1204.istock.model.dto.SinaStockPriceDto;
import io.github.kingschan1204.istock.model.dto.StockMasterDto;
import io.github.kingschan1204.istock.model.dto.ThsStockDividendRate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingschan on 2017/6/28.
 */
public class StockSpilderUtil {

    private static Logger log = LoggerFactory.getLogger(StockSpilderUtil.class);
    public static final String regexNumber = "^[-+]?([0]{1}(\\.[0-9]+)?|[1-9]{1}\\d*(\\.[0-9]+)?)";//"^[-+]?[0-9]+(\\.[0-9]+)?$";

    /**
     * 将股票代码转换成新浪接口的格式http://hq.sinajs.cn/list=
     * sh 上海  sz 深圳
     *
     * @param code
     * @return
     */
    public static String formatSinaQuryStockCode(String code) {
        //上证
        if (code.startsWith("60")) {
            return String.format("sh%s", code);
        }
        //5开头，沪市基金或权证
        else if(code.startsWith("5")){
            return String.format("sh%s", code);
        }
        //1开头的，是深市基金
        else if(code.startsWith("1")){
            return String.format("sz%s", code);
        }
        //深证
        else if (code.startsWith("00")) {
            return String.format("sz%s", code);
        }
        return null;
    }

    /**
     * 格式化数据，如果不是数字全部返回-1
     *
     * @param value
     * @return
     */
    public static Double mathFormat(String value) {
        String v = value.replaceAll("\\%", "").replace("亿", "");
        if (v.matches(regexNumber)) {
            return Double.valueOf(v);
        }
        return -1D;
    }

    /**
     * 调用新浪数据接口返回当前价格
     *
     * @param stockCode
     * @return
     * @throws Exception
     */
    public static List<SinaStockPriceDto> getStockPrice(String[] stockCode) throws Exception {
        StringBuffer queryStr = new StringBuffer();
        for (String code : stockCode) {
            String resultCode = formatSinaQuryStockCode(code);
            if (null != resultCode) {
                queryStr.append(resultCode).append(",");
            }
        }
        String queryCode = queryStr.toString().replaceAll("\\,$", "");
        String query = String.format("http://hq.sinajs.cn/list=%s", queryCode);
        log.info(query);
        String content = Jsoup.connect(query).ignoreContentType(true).get().text();
        String[] line = content.split(";");
        List<SinaStockPriceDto> list = new ArrayList<SinaStockPriceDto>();
        for (String s : line) {
            String row = s.trim().replaceAll("^var\\D+|\"", "").replace("=", ",");
            String data[] = row.split(",");
            double xj = Double.parseDouble(data[4]);
            double zs = Double.parseDouble(data[3]);
            double zf = (xj - zs) / zs * 100;
            log.info(String.format("%s %s 现价:%s 昨收:%s 涨幅:%.2f%s", data[0], data[1], data[3], data[2], zf, "%"));
            //String code,String name,Double price,Double yprice,Double rangePrice
            NumberFormat nf = NumberFormat.getNumberInstance();
            // 保留两位小数
            nf.setMaximumFractionDigits(2);
            // 如果不需要四舍五入，可以使用RoundingMode.DOWN
            nf.setRoundingMode(RoundingMode.UP);
            list.add(new SinaStockPriceDto(data[0], data[1], xj, zs, Double.valueOf(nf.format(zf))));
        }
        return list;
    }

    /**
     * 基本信息
     *
     * @param code
     * @throws Exception
     */
    public static StockMasterDto getStockInfo(String code) throws Exception {
        String url = String.format("http://basic.10jqka.com.cn/16/%s/", code);
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
        String jzcsyl = "-1";
        if(tds1.size()>14){
            jzcsyl=tds1.get(14).select("span").get(1).text();//净资产收益率
        }
        StockMasterDto dto = new StockMasterDto();
        dto.setsMainBusiness(zyyw);
        dto.setsIndustry(sshy);
        dto.setsPeDynamic(BigDecimal.valueOf(mathFormat(dtsyl)));
        dto.setsPeStatic(BigDecimal.valueOf(mathFormat(sjljt)));
        dto.setsPb(BigDecimal.valueOf(mathFormat(sjl)));
        dto.setsTotalValue(BigDecimal.valueOf(mathFormat(zsz)));
        dto.setsRoe(BigDecimal.valueOf(mathFormat(jzcsyl)));
        return dto;
    }

    /**
     * 分红列表
     *
     * @param code
     * @throws Exception
     */
    public static List<ThsStockDividendRate> getStockDividendRate(String code) throws Exception {
        String url = String.format("http://basic.10jqka.com.cn/16/%s/bonus.html", code);
        Document doc = Jsoup.connect(url).get();
        Element table = doc.getElementById("bonus_table");
        if (null != table) {
            Elements rows = table.getElementsByTag("tr");
            //报告期	董事会日期	股东大会预案公告日期	实施日期	分红方案说明	A股股权登记日	A股除权除息日	方案进度	股利支付率	分红率
            List<ThsStockDividendRate> list = new ArrayList<ThsStockDividendRate>();
            for (int i = 1; i < rows.size(); i++) {
                String[] data = rows.get(i).select("td").text().split(" ");
                if (data[0].endsWith("年报")) {
                    log.info(String.format("%s|%s|%s|%s", data[0], data[6], data[4], data[9]));
                    //String year, String date, String plan, Double percent
                    double value = -1;
                    if (null != data[9]) {
                        String temp = data[9].replace("%", "");
                        if (temp.matches(regexNumber))
                            value = Double.parseDouble(temp);
                    }
                    list.add(new ThsStockDividendRate(data[0], data[6], data[4], value));
                }
            }
            return list;
        }
        return null;
    }

   /* public static void main(String[] args) throws Exception {
        String[] stockCode = {"600741"};//股票代码
        for (String s : stockCode) {
            *//*StockSpilderUtil.getStockPrice(new String[]{s});
            System.out.println(new ObjectMapper().writeValueAsString(StockSpilderUtil.getStockInfo(s)));*//*
            getStockDividendRate(s);
        }


    }*/
}
