package io.github.kingschan1204.istock.test;

import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.DefaultSpiderImpl;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * spider test
 * @author chenguoxiang
 * @create 2018-02-01 10:05
 **/
@SpringBootTest
public class DefaultSpiderImplTest {
    StockSpider spider=new DefaultSpiderImpl();
    private static final String code="600519";
    @Test
    public void getStockPrice() throws Exception {
        System.out.println(spider.getStockPrice(new String[]{code}).toJSONString());
    }

    @Test
    public void getStockInfo() throws Exception {
        System.out.println(spider.getStockInfo(code).toJSONString());
    }

    @Test
    public void getHistoryDividendRate() throws Exception {
        System.out.println(spider.getHistoryDividendRate(code).toJSONString());
    }

    @Test
    public void getHistoryROE() throws Exception {
        System.out.println(spider.getHistoryROE(code));
    }

    @Test
    public void getHistoryPE() throws Exception {
        System.out.println(spider.getHistoryPE(code).toJSONString());
    }

    @Test
    public void getHistoryPB() throws Exception {
        System.out.println(spider.getHistoryPB(code).toJSONString());
    }

}