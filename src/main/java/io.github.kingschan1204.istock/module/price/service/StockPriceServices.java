package io.github.kingschan1204.istock.module.price.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.info.service.StockInfoService;
import io.github.kingschan1204.istock.module.price.po.StockPrice;
import io.github.kingschan1204.istock.module.price.repository.StockPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Stock price services
 *
 * @author chenguoxiang
 * @create 2018-02-01 16:14
 **/
@Service
public class StockPriceServices {
    Logger log = LoggerFactory.getLogger(StockPriceServices.class);
    @Autowired
    private StockPriceRepository repository;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockSpider spider;
    @Autowired
    private StockInfoService infoService;

    /**
     * add stock code
     *
     * @param codes
     * @throws Exception
     */
    public void addStock(String... codes) throws Exception {
        JSONArray jsons = spider.getStockPrice(codes);
        List<StockPrice> lis = JSON.parseArray(jsons.toJSONString(), StockPrice.class);
        List<String> listCode = lis.stream().map(StockPrice -> StockPrice.getCode()).distinct().collect(Collectors.toList());
        Long count = template.count(new Query(Criteria.where("code").in(listCode)), StockPrice.class);
        if(count>0){throw new Exception("要添加的代码已存在！");}
        lis.stream().forEach(stockPrice -> repository.save(stockPrice));
        infoService.addStock(codes);
    }


}
