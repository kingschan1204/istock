package io.github.kingschan1204.istock.module.maindata.services;

import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.module.spider.openapi.TushareApi;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码信息服务类
 * @author chenguoxiang
 * @create 2018-10-30 15:22
 **/
@Slf4j
@Service
public class StockCodeInfoService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TushareApi tushareSpider;


    /**
     * 代码列表刷新
     */
    public void refreshCode(){
        List<StockCodeInfo> list = new ArrayList<StockCodeInfo>();
        JSONArray rows =tushareSpider.getStockCodeList();
        for (int i = 0; i < rows.size(); i++) {
            list.add(new StockCodeInfo(rows.getJSONArray(i)));
        }
        //先删除再新增
        mongoTemplate.dropCollection(StockCodeInfo.class);
        mongoTemplate.insertAll(list);
    }

    /**
     * 返回所有代码
     *
     * @return
     */
    public List<StockCodeInfo> getAllStockCodes() {
        return mongoTemplate.find(new Query(), StockCodeInfo.class);

    }

    /**
     * 返回沪市代码
     * @return
     */
    public List<StockCodeInfo> getSHStockCodes() {
        return mongoTemplate.find(new Query(Criteria.where("type").is("sh")), StockCodeInfo.class);

    }

    /**
     * 返回深市代码
     * @return
     */
    public List<StockCodeInfo> getSZStockCodes() {
        return mongoTemplate.find(new Query(Criteria.where("type").is("sz")), StockCodeInfo.class);
    }
}
