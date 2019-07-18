package io.github.kingschan1204.istock.common.util.stock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬虫接口定义
 *
 * @author chenguoxiang
 * @create 2018-01-30 18:00
 **/
public interface StockSpider {


    /**
     * 将股票代码转换成新浪接口的格式http://hq.sinajs.cn/list=
     * sh 上海  sz 深圳
     *
     * @param code
     * @return
     */
    static String formatStockCode(String code) {
        if (code.matches("^(sz|sh)\\d{6}$")) {
            return code;
        }
        //5开头，沪市基金或权证 60开头上证  68开头科创板
        else if (code.matches("^60.*|68.*|^5.*")) {
            return String.format("sh%s", code);
        }
        //1开头的，是深市基金 00开头是深圳
        else if (code.matches("^1.*|^00.*|^300...")) {
            return String.format("sz%s", code);
        }
        return null;
    }

    /**
     * 提取文本中匹配正则的字符串
     *
     * @param text
     * @param regx 正则
     * @return 多个结果已, 分隔
     */
    static String findStrByRegx(String text, String regx) {
        StringBuffer bf = new StringBuffer(64);
        try {
            Pattern pattern = Pattern.compile(regx);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                bf.append(matcher.group()).append(",");
            }
            int len = bf.length();
            if (len > 0) {
                bf.deleteCharAt(len - 1);
            }
            return bf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 是否工作日
     * @param date
     * @return true 工作日 false 非工作日
     * @throws IOException
     */
    static boolean isWorkDay(Integer date) throws IOException {
        String api =String.format("http://api.goseek.cn/Tools/holiday?date=%s",date);
        String result = Jsoup.connect(api).timeout(3000).ignoreContentType(true).get().text();
        //{"code":10001,"data":2}  工作日对应结果为 0, 休息日对应结果为 1, 节假日对应的结果为 2
        JSONObject json = JSON.parseObject(result);
        return json.getIntValue("data")==0;
    }


    /**
     * 提取文本中匹配正则的字符串
     *
     * @param text 源字符串
     * @param regx 正则
     * @return
     */
    static List<String> findStringByRegx(String text, String regx) {
        List<String> list = new ArrayList<String>();
        try {
            Pattern pattern = Pattern.compile(regx);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                list.add(matcher.group());
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }

    }



    /**
     * 启用ssl
     *
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }




    /**
     * 得到指定代码历史分红
     *
     * @param code
     * @return
     * @throws Exception
     */
    JSONArray getHistoryDividendRate(String code) throws Exception;


    /**
     * 得到历史ROE
     *
     * @param code
     * @return
     * @throws Exception
     */
    JSONArray getHistoryROE(String code) throws Exception;



    /**
     * 得到上海所有代码
     *
     * @return
     * @throws Exception
     */
    List<String> getStockCodeBySH() throws Exception;


    /**
     * 得到深圳所有代码
     *
     * @return
     * @throws Exception
     */
    List<String> getStockCodeBySZ() throws Exception;


}
