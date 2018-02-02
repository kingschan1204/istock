package io.github.kingschan1204.istock.module.price.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * ${DESCRIPTION}
 *
 * @author chenguoxiang
 * @create 2018-02-01 17:15
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockPriceServicesTest {

    @Autowired
    StockPriceServices services;

    @Test
    public void addStock() throws Exception {
        services.addStock(new String[]{"600016","601633","600548","600398"});
    }

}