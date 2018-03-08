package io.github.kingschan1204.istock.common.util.stock;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * 爬虫接口定义
 * @author chenguoxiang
 * @create 2018-01-30 18:00
 **/
public interface StockSpider {

     static final String regexNumber = "^[-+]?([0]{1}(\\.[0-9]+)?|[1-9]{1}\\d*(\\.[0-9]+)?)";//"^[-+]?[0-9]+(\\.[0-9]+)?$";

    /**
     * 将股票代码转换成新浪接口的格式http://hq.sinajs.cn/list=
     * sh 上海  sz 深圳
     * @param code
     * @return
     */
     static String formatStockCode(String code) {
         if(code.matches("^(sz|sh)\\d{6}$")){
             return code;
         }
        //5开头，沪市基金或权证 60开头上证
       else if (code.matches("^60.*|^5.*")) {
            return String.format("sh%s", code);
        }
        //1开头的，是深市基金 00开头是深圳
        else if(code.matches("^1.*|^00.*|^300...")){
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
     static Double mathFormat(String value) {
        String v =value.replaceAll("[^0-9|\\.]","");
        //value.replaceAll("\\%", "").replace("亿", "");
        if (v.matches(regexNumber)) {
            return Double.valueOf(v);
        }
        return -1D;
    }

    /**
     * 启用ssl
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
     static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
            public X509Certificate[] getAcceptedIssuers(){return new X509Certificate[0];}
            public void checkClientTrusted(X509Certificate[] certs, String authType){}
            public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }


    /**
     * 得到指定代码的价格
     * @param stockCode
     * @return
     */
      JSONArray getStockPrice(String[] stockCode)throws Exception;


    /**
     * 得到指定代码的基本信息
     * @param code
     * @return
     * @throws Exception
     */
     JSONObject getStockInfo(String code) throws Exception;

    /**
     * 得到指定代码历史分红
     * @param code
     * @return
     * @throws Exception
     */
      JSONArray getHistoryDividendRate(String code) throws Exception;


    /**
     * 得到历史ROE
     * @param code
     * @return
     * @throws Exception
     */
    JSONArray getHistoryROE(String code) throws Exception;


    /**
     * 得到历史PE
     * @param code
     * @return
     * @throws Exception
     */
    JSONArray getHistoryPE(String code) throws Exception;


    /**
     * 得到历史pb
     * @param code
     * @return
     * @throws Exception
     */
    JSONArray getHistoryPB(String code) throws Exception;


    /**
     * 得到所有代码
     * @return
     * @throws Exception
     */
    List<String> getAllStockCode()throws Exception;

}
