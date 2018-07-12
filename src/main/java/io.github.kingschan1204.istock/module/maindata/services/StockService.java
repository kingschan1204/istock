package io.github.kingschan1204.istock.module.maindata.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.JisiluSpilder;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockDividend;
import io.github.kingschan1204.istock.module.maindata.po.StockHisDividend;
import io.github.kingschan1204.istock.module.maindata.po.StockHisRoe;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisDividendRepository;
import io.github.kingschan1204.istock.module.maindata.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stock service
 * @author chenguoxiang
 * @create 2018-03-27 10:27
 **/
@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockHisDividendRepository stockHisDividendRepository;
    @Autowired
    private StockHisRoeService stockHisRoeService;
    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private JisiluSpilder jisiluSpilder;

    /**
     * add stock code
     *
     * @param codes
     * @throws Exception
     */
    public void addStock(String... codes) throws Exception {
        JSONArray jsons = spider.getStockPrice(codes);
        for (int i = 0; i < jsons.size(); i++) {
            JSONObject json = jsons.getJSONObject(i);
            String scode=json.getString("code");
            JSONObject info=spider.getStockInfo(scode);
            //"date":"2017-07-07","code":"600519","year":"2016年报","executeDate":"2017-07-01","remark":"10派67.87元(含税)","percent":1.44}
            JSONArray dividends=spider.getHistoryDividendRate(scode);
            JSONObject dividend;
            String date="";
            Double percent=0D;
            if(null!=dividends&&dividends.size()>0){
                for (int j = 0; j < dividends.size(); j++) {
                    if(dividends.getJSONObject(j).getDouble("percent")>0){
                        percent=dividends.getJSONObject(j).getDoubleValue("percent");
                        date=dividends.getJSONObject(j).getString("date");
                        break;
                    }
                }
                //save dividend
                List<StockHisDividend> stockHisDividendList = JSONArray.parseArray(dividends.toJSONString(),StockHisDividend.class);
                template.remove(new Query(Criteria.where("code").is(scode)),StockHisDividend.class);
//                stockHisDividendRepository.save(stockHisDividendList);
            }
            json.put("dividend",percent);
            json.put("dividendDate",date);
            json.putAll(info);
            // his roe
            stockHisRoeService.addStockHisRoe(scode);
        }
        List<Stock> list = JSON.parseArray(jsons.toJSONString(), Stock.class);
        stockRepository.save(list);
    }




    public String queryStock(int pageindex, int pagesize, final String pcode,final String type,String pb,String dy, String orderfidld, String psort){
        DBObject dbObject = new BasicDBObject();
        DBObject fieldObject = new BasicDBObject();
        fieldObject.put("todayMax", false);
        fieldObject.put("todayMin", false);
        fieldObject.put("priceDate", false);
        fieldObject.put("mainBusiness", false);
        fieldObject.put("dyDate", false);
        fieldObject.put("infoDate", false);
        fieldObject.put("dividendUpdateDay", false);
        Query query = new BasicQuery(dbObject,fieldObject);
        Optional<String> code =Optional.ofNullable(pcode);
        if (code.isPresent()){
            if(pcode.matches("\\d{6}")){
                query.addCriteria(Criteria.where("_id").is(pcode));
            }else if(pcode.matches("\\d+")){
                query.addCriteria(Criteria.where("_id").regex(pcode));
            }
            else{
                query.addCriteria(Criteria.where("name").regex(pcode));
            }
        }
        if(null!=type&&type.matches("sz|sh")){
            query.addCriteria(Criteria.where("type").is(type));
        }
        if(null!=pb&&pb.matches("\\d+(\\.\\d+)?\\-\\d+(\\.\\d+)?")){
            double s = Double.parseDouble(pb.split("-")[0]);
            double d = Double.parseDouble(pb.split("-")[1]);
            query.addCriteria(Criteria.where("pb").gte(s).lte(d));
        }
        if(null!=dy&&dy.matches("(dy|dividend|fiveYearDy)\\-\\d+(\\.\\d+)?\\-\\d+(\\.\\d+)?")){
            String field=dy.split("-")[0];
            double s = Double.parseDouble(dy.split("-")[1]);
            double d = Double.parseDouble(dy.split("-")[2]);
            query.addCriteria(Criteria.where(field).gte(s).lte(d));
        }
        //记录总数
        Long total=template.count(query,Stock.class);
        //分页
        query.skip((pageindex-1)*pagesize).limit(pagesize);
        //排序
        List<Sort.Order> orders = new ArrayList<Sort.Order>();  //排序
        orders.add(new Sort.Order(
                psort.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC
                ,orderfidld));
        Sort sort = new Sort(orders);
        query.with(sort);
        //code
        List<Stock> list =template.find(query,Stock.class);
        JSONArray jsons = JSONArray.parseArray(JSONArray.toJSONString(list));
        JSONObject temp;
        for (int i = 0; i <jsons.size() ; i++) {
            temp =jsons.getJSONObject(i);
            temp.put("fluctuate",temp.getString("fluctuate")+"%");

            if(temp.containsKey("roe")){
                if(temp.getDouble("roe")!=-1){
                    temp.put("roe",temp.getString("roe")+"%");
                }else {
                    temp.put("roe","--");
                }

            }
            if(temp.containsKey("totalValue")&&temp.getDouble("totalValue")!=-1){
                temp.put("totalValue",temp.getString("totalValue")+"亿");
            }
            if(temp.containsKey("pb")&&temp.getDouble("pb")==-1){
                temp.put("pb","--");
            }
            if(temp.containsKey("pes")&&temp.getDouble("pes")==-1){
                temp.put("pes","--");
            }
            if(temp.containsKey("ped")&&temp.getDouble("ped")==-1){
                temp.put("ped","--");
            }
            if(temp.containsKey("bvps")&&temp.getDouble("bvps")==-1){
                temp.put("bvps","--");
            }
            if(temp.containsKey("dividend")){
                if(temp.getDouble("dividend")!=-1){
                    temp.put("dividend",temp.getString("dividend")+"%");
                }
            }else {
                temp.put("dividend","--");
            }
            if(temp.containsKey("dy")){
                temp.put("dy",temp.getString("dy")+"%");
            }
            if(temp.containsKey("fiveYearDy")){
                temp.put("fiveYearDy",temp.getString("fiveYearDy")+"%");
            }

        }

        JSONObject data= new JSONObject();
        long pagetotal=total%pagesize==0?total/pagesize:total/pagesize+1;
        data.put("rows",jsons);
        data.put("total",pagetotal);//有多少页
        data.put("records",total);// 总共有多少条记录
        data.put("page",pageindex);
        return data.toJSONString();
    }


    /**
     *
     * @param code
     * @return
     */
    public  List<StockDividend> getStockDividend(String code){
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        //排序
        List<Sort.Order> orders = new ArrayList<Sort.Order>();  //排序
        orders.add(new Sort.Order(Sort.Direction.ASC,"title"));
        Sort sort = new Sort(orders);
        query.with(sort);
        //code
        List<StockDividend> list =template.find(query,StockDividend.class);
        return list;
    }


    public List<StockHisRoe> getStockHisRoe(String code){
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        //排序
        List<Sort.Order> orders = new ArrayList<Sort.Order>();  //排序
        orders.add(new Sort.Order(Sort.Direction.ASC,"year"));
        Sort sort = new Sort(orders);
        query.with(sort);
        //code
        List<StockHisRoe> list =template.find(query,StockHisRoe.class);
       return list;
    }



    /**
     * 抓取历史数据
     * @param code
     * @return
     * @throws Exception
     */
    public List<String> crawAndSaveHisPbPe(String code)throws Exception{

        StringBuilder price = new StringBuilder();
        StringBuilder pe = new StringBuilder();
        StringBuilder pb = new StringBuilder();
        StringBuilder date = new StringBuilder();

        JSONObject data= jisiluSpilder.crawHisPbPePriceAndReports(code);
        List<DBObject> list = new ArrayList<>();
        DBCollection hisdata = template.getCollection("stock_his_pe_pb");
        DBCollection report = template.getCollection("stock_report");

        JSONArray hisdataJsons=data.getJSONArray("hisdata");
        for (int i = 0; i <hisdataJsons.size() ; i++) {
            JSONObject row = hisdataJsons.getJSONObject(i);
            DBObject object = new BasicDBObject();
            object.put("code",code);
            object.put("date",row.getString("date"));
            object.put("pb",row.getDouble("pb"));
            object.put("pe",row.getDouble("pe"));
            object.put("price",row.getDouble("price"));
            //顺便拼成字符串
            date.append("'").append(row.getString("date")).append("'");
            price.append(row.getDouble("price"));
            pb.append(row.getDouble("pb"));
            pe.append(row.getDouble("pe"));
            if(i!=hisdataJsons.size()-1){
                price.append(",");
                pb.append(",");
                pe.append(",");
                date.append(",");
            }

            list.add(object);
            if ((i != 0 && i %1000 == 0)||i==hisdataJsons.size()-1){
                hisdata.insert(list);
                list.clear();
            }
        }


        JSONArray reportJsons=data.getJSONArray("reports");
        list = new ArrayList<>();
        for (int i = 0; i <reportJsons.size() ; i++) {
            JSONObject row = reportJsons.getJSONObject(i);
            DBObject object = new BasicDBObject();
            object.put("code",code);
            object.put("releaseDay",row.getString("releaseDay"));
            object.put("link",row.getString("link"));
            object.put("title",row.getString("title"));
            list.add(object);
            if ((i != 0 && i %1000 == 0)||i==reportJsons.size()-1){
                report.insert(list);
                list.clear();
            }
        }
        //价格》日期》pb》pe
        List<String> result = new ArrayList<>();
        result.add(price.toString());
        result.add(date.toString());
        result.add(pb.toString());
        result.add(pe.toString());
        result.add(reportJsons.toJSONString());
        return result;
    }

}
