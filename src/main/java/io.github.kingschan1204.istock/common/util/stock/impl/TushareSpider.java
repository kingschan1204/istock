package io.github.kingschan1204.istock.common.util.stock.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * 用挖地免的接口
 * @author chenguoxiang
 * @create 2018-10-30 9:31
 **/
@RefreshScope
@Component("TushareSpider")
public class TushareSpider  {

    @Value("${tushare.token}")
    private String tuToken="66cdab7a757dcb728f8833f732a928791d67b6f38e1cd8d94bb79a0d";
    @Autowired
    private RestTemplate restTemplate=new RestTemplate();
    final String api="http://api.tushare.pro";

    /**
     * 格式化成tushare API所需格式
     * @param code
     * @return
     */
   public  static String formatCode(String code) {
        //5开头，沪市基金或权证 60开头上证
        if (code.matches("^60.*|^5.*")) {
            return String.format("%s.SH", code);
        }
        //1开头的，是深市基金 00开头是深圳
        else if (code.matches("^1.*|^00.*|^300...")) {
            return String.format("%s.SZ", code);
        }
        return null;
    }

    /**
     * post 方式提交
     * @param params
     * @return
     */
    String post(JSONObject params){
        HttpHeaders headers = new HttpHeaders();
        //定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        params.put("token", tuToken);
        HttpEntity<String> formEntity = new HttpEntity<String>(params.toString(), headers);
        String result = restTemplate.postForObject(api, formEntity, String.class);
        return result;
    }


    /**
     * 返回已上市的A股代码
     * @return TS代码,股票代码,股票名称,所在地域,所属行业,股票全称,市场类型 （主板/中小板/创业板）,上市状态： L上市 D退市 P暂停上市,上市日期
     */
    public JSONArray getStockCodeList(){
        JSONObject json = new JSONObject();
        //接口名称
        json.put("api_name","stock_basic");
        //只取上市的
        json.put("params",JSON.parse("{'list_status':'L'}"));
        json.put("fields","ts_code,symbol,name,area,industry,fullname,market,list_status,list_date");
        String result = post(json);
        JSONObject datas= JSON.parseObject(result);
        JSONArray items =datas.getJSONObject("data").getJSONArray("items");
        return items;
    }

    /**
     *获取上海公司基础信息
     * @return
     */
    public JSONArray getStockShCompany(){
        JSONObject json = new JSONObject();
        //接口名称
        json.put("api_name","stock_company");
        json.put("params",JSON.parse("{'exchange':'SSE'}"));
        json.put("fields","ts_code,chairman,manager,secretary,reg_capital,setup_date,province,city,introduction,website,email,office,employees,main_business,business_scope");
        String result = post(json);
        JSONObject data= JSON.parseObject(result);
        JSONArray items =data.getJSONObject("data").getJSONArray("items");
        return items;
    }

    /**
     *获取深圳公司基础信息
     * @return
     */
    public JSONArray getStockSZCompany(){
        JSONObject json = new JSONObject();
        //接口名称
        json.put("api_name","stock_company");
        json.put("params",JSON.parse("{'exchange':'SZSE'}"));
        json.put("fields","ts_code,chairman,manager,secretary,reg_capital,setup_date,province,city,introduction,website,email,office,employees,main_business,business_scope");
        String result = post(json);
        JSONObject data= JSON.parseObject(result);
        JSONArray items =data.getJSONObject("data").getJSONArray("items");
        return items;
    }

    /**
     * 得到前10大持有人
     * @param code
     * @return
     */
    public JSONArray getStockTopHolders(String code){
        JSONObject json = new JSONObject();
        //接口名称
        json.put("api_name","top10_holders");
        json.put("params",JSON.parse(String.format("{'ts_code':'%s'}",code)));
        json.put("fields","ts_code,ann_date,end_date,holder_name,hold_amount,hold_ratio");
        String result = post(json);
        JSONObject datas= JSON.parseObject(result);
        JSONArray items =datas.getJSONObject("data").getJSONArray("items");
        return items;
    }
    public static void main(String[] args) {
        try {
            //{"date":"2018-11-01 18:06:25","code":200,"address":"湖南省长沙市 电信","ip":"113.246.64.67"}
            System.setProperty("https.maxRedirects", "50");
            System.getProperties().setProperty("https.proxySet", "true");
            System.getProperties().setProperty("https.proxyHost", "114.116.10.21");
            System.getProperties().setProperty("https.proxyPort", "3128");
            StockSpider.enableSSLSocket();
            String json =Jsoup.connect("https://api.ttt.sh/ip/qqwry/").get().text();
            JSONObject j = JSON.parseObject(json);
            System.out.println(j);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
