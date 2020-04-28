package io.github.kingschan1204.istock.common.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author chenguoxiang
 * @create 2019-08-21 16:45
 **/
@Slf4j
@Component
public class MyMongoTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存
     *
     * @param obj
     */
    public void save(Object obj) {
        mongoTemplate.save(obj);
    }

    /**
     * 批量保存
     *
     * @param list  数据
     * @param clazz 数据模型
     * @return
     */
    public BulkWriteResult save(List<?> list, Class clazz) {
        BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, clazz);
        for (int i = 0; i < list.size(); i++) {
            ops.insert(list.get(i));
        }
        return ops.execute();
    }

    /**
     * 删除一个集合
     *
     * @param clazz
     */
    public void dropCollection(Class clazz) {
        mongoTemplate.dropCollection(clazz);
    }


    /**
     * 分组
     *
     * @param collection 集合名称
     * @param keys       分组键
     * @return
     */
    public JSONArray groupBy(Criteria criteria, String collection, String keys) {
        String where ="{ $match: #{where}},";
        String nowhere="{  aggregate: \"#{table}\",  pipeline: [ #{match} { $group: { _id: \"$#{key}\", count: { $sum : 1 } } }  ],  cursor: { batchSize:9000} }";
        //null==criteria?"":criteria.getCriteriaObject().toJson()
        String cmd =null;
        if(null==criteria){
            cmd=nowhere.replace("#{match}","");
        }else{
            cmd=nowhere.replace("#{match}",where.replace(" #{where}",criteria.getCriteriaObject().toJson().toString()));
        }
        cmd=cmd.replace("#{table}",collection)
                .replace("#{key}",keys);
       Document doc= mongoTemplate.executeCommand(cmd);
       JSONObject json =JSON.parseObject(doc.toJson().toString());
       return json.getJSONObject("cursor").getJSONArray("firstBatch");
    }

    /**
     * mongo分页查询
     *
     * @param pageindex 第几页
     * @param pagesize  每页显示多少条
     * @param clazz     查询哪一个对象
     * @param sort      排序方式
     * @param sortField 排序字段
     * @param criterias 条件
     * @return
     */
    public Page pageQuery(int pageindex, int pagesize, Class clazz, Sort.Direction sort, String sortField, Criteria... criterias) {
        Query query = new Query();
        if(null!=criterias){
            for (Criteria where : criterias) {
                query.addCriteria(where);
            }
        }
        Long total = mongoTemplate.count(query, clazz);
        //分页
        query.skip((pageindex - 1) * pagesize).limit(pagesize);
        //排序
        if (Optional.ofNullable(sortField).isPresent()) {
            query.with( Sort.by(sort, sortField));
        }
        //code
        List<?> list = mongoTemplate.find(query, clazz);
        long pagetotal = total % pagesize == 0 ? total / pagesize : total / pagesize + 1;
        return new Page(total,pagetotal, pageindex, pagesize, list);

    }

    /**
     * 查询
     *
     * @param clazz     查询的集合
     * @param sort      是否排序
     * @param sortField 排序的字段名
     * @param limit 限制显示条数 为null则不限制
     * @param criterias 条件集合
     * @return
     */
    public List<?> query(Class clazz, Sort.Direction sort, String sortField,Integer limit, Criteria... criterias) {
        Query query = new Query();
        for (Criteria where : criterias) {
            query.addCriteria(where);
        }
        //排序
        if (Optional.ofNullable(sortField).isPresent()) {
            query.with(Sort.by(sort,sortField));
        }
        if(null!=limit){
            query.limit(limit);
        }
        //code
        List<?> list = mongoTemplate.find(query, clazz);
        return list;
    }

    /**
     *  更新符合条件的第一条数据
     * @param collection  表名
     * @param updateObj 更新的数据
     * @param criteria  条件
     * @return
     */
    public Boolean updateFirst(String collection, JSONObject updateObj, Criteria... criteria) {
        Query query = new Query();
        for (Criteria c : criteria) {
            query.addCriteria(c);
        }
        Update update = new Update();
        for (String key :updateObj.keySet()) {
            update.set(key,updateObj.get(key));
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, collection);
        return updateResult.getModifiedCount()>0;
    }

    /**
     * 删除
     * @param clazz
     * @param criteria
     * @return
     */
    public long remove(Class clazz,Criteria... criteria){
        Query query = new Query();
        for (Criteria c : criteria) {
            query.addCriteria(c);
        }
        return  mongoTemplate.remove(query,clazz).getDeletedCount();

    }


}
