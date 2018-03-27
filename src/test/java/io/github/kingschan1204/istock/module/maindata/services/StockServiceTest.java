package io.github.kingschan1204.istock.module.maindata.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ${DESCRIPTION}
 *
 * @author chenguoxiang
 * @create 2018-03-27 10:47
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockServiceTest {
    @Autowired
    private StockService stockService;

    @Test
    public void addStock() throws Exception {
        stockService.addStock(new String[]{
                "000895","601288","000338","601668","601633"
        });
    }
    @Test
    public void findall(){
        stockService.queryStock( 1, 3, "", "code", "desc");
    }

}