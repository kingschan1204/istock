package io.github.kingschan1204.istock.module.maindata.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.StockHisRoe;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisRoeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author chenguoxiang
 * @create 2018-03-09 14:57
 **/
@Service
public class StockHisRoeService {

    @Autowired
    private StockSpider spider;
    @Autowired
    private StockHisRoeRepository repository;
    @Autowired
    private MongoTemplate template;

    /**
     * 增加一个代码的历史roe
     * @param code
     * @throws Exception
     */
    public List<StockHisRoe> addStockHisRoe(String code) throws Exception {
        JSONArray jsons=spider.getHistoryROE(code);
        List<StockHisRoe> lis = JSON.parseArray(jsons.toJSONString(),StockHisRoe.class);
        template.remove(new Query(Criteria.where("code").is(code)),StockHisRoe.class);
        repository.save(lis);
        return lis;

    }
}
