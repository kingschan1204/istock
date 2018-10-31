package io.github.kingschan1204.istock.common.util.stock.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
    private String tuToken;
    @Autowired
    private RestTemplate restTemplate;
    final String api="http://api.tushare.pro";


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
     *获取上市公司基础信息
     * @return
     */
    public JSONArray getStockCompany(){
        JSONObject json = new JSONObject();
        //接口名称
        json.put("api_name","stock_company");
        json.put("fields","ts_code,chairman,manager,secretary,reg_capital,setup_date,province,city,introduction,website,email,office,employees,main_business,business_scope");
        String result = post(json);
        JSONObject datas= JSON.parseObject(result);
        JSONArray items =datas.getJSONObject("data").getJSONArray("items");
        return items;
    }

    public static void main(String[] args) {
        System.out.println(new TushareSpider().getStockCompany());

    }
}
