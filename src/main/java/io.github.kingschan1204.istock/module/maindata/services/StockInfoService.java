package io.github.kingschan1204.istock.module.maindata.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.github.kingschan1204.istock.module.maindata.po.StockTopHolders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author chenguoxiang
 * @create 2018-11-02 14:17
 **/
@Service
public class StockInfoService {
    private Logger log = LoggerFactory.getLogger(StockInfoService.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查公司详细信息
     * @param code
     * @return
     */
    public JSONObject getStockInfo(String code) {
        //设置lookup
        LookupOperation lookupOperation = LookupOperation.newLookup().from("stock_code_info").localField("_id").foreignField("_id").as("info");
        LookupOperation lookupOperation1 = LookupOperation.newLookup().from("stock_company").localField("_id").foreignField("_id").as("company");
        //LookupOperation lookupOperation2 = LookupOperation.newLookup().from("stock_top_holders").localField("_id").foreignField("code").as("doc2");

        //这里分三个部分，先lookup；结果再用project筛选，如果不筛选user里面所有内容，包括密码都会输出；最后sort排序
        Aggregation aggregation = Aggregation.newAggregation(
                lookupOperation, lookupOperation1,
                //project("id", "demographic", "sizes", "userDoc.realname"),
                //Sort(Sort.Direction.ASC, "demographic")
                new MatchOperation(Criteria.where("_id").is(code))
        );
        //看具体的查询，有助于理解各个参数的影响
        log.info(aggregation.toString());
        //正式查询
        AggregationResults<BasicDBObject> results = mongoTemplate.aggregate(aggregation, "stock", BasicDBObject.class);
        List<BasicDBObject> list = results.getMappedResults();
        JSONArray jsonArray=JSONArray.parseArray(JSON.toJSONString(list));
        JSONObject data =jsonArray.getJSONObject(0);
        //查股东
        DBObject dbObject = new BasicDBObject();
        DBObject fieldObject = new BasicDBObject();
        fieldObject.put("_id", false);
        fieldObject.put("code", false);
        Query query = new BasicQuery(dbObject,fieldObject);
        query.addCriteria(Criteria.where("code").is(code));
        List<StockTopHolders> holders=  mongoTemplate.find(query, StockTopHolders.class);
        /*Map<String,List<StockTopHolders>> map =holders.stream().collect(Collectors.groupingBy(StockTopHolders::getHolderName));
        holders=new ArrayList<StockTopHolders>();
        map.forEach((key, value) ->{
            System.out.println(JSON.toJSON(value));
        });

        System.out.println(map);*/
        JSONArray jsonHolders=JSONArray.parseArray(JSON.toJSONString(holders));
        data.put("holders",jsonHolders);
        return data;

    }



}
