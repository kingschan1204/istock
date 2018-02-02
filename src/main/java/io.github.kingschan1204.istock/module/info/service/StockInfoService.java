package io.github.kingschan1204.istock.module.info.service;

import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.info.po.StockInfo;
import io.github.kingschan1204.istock.module.info.repository.StockInfoReository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * StockInfoService
 *
 * @author chenguoxiang
 * @create 2018-02-02 14:41
 **/
@Service
public class StockInfoService {
    Logger log = LoggerFactory.getLogger(StockInfoService.class);
    @Autowired
    private StockInfoReository repository;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockSpider spider;

    /**
     * add stock
     * @param codes
     */
    public void addStock(String ... codes){
        Arrays.stream(codes).forEach(code -> {
            try {
                StockInfo stockInfo =spider.getStockInfo(code).toJavaObject(StockInfo.class);
                repository.save(stockInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
