package io.github.kingschan1204.istock.module.maindata.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author chenguoxiang
 * @create 2018-03-27 10:47
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockServiceTest {
    @Autowired
    private StockService stockService;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockCodeService stockCodeService;

    @Test
    public void saveAllCode() throws Exception {
        Long start =System.currentTimeMillis();
        stockCodeService.saveAllStockCode();
        System.out.println(String.format("更新代码共耗时：%s ms",(System.currentTimeMillis()-start)));
    }





}