package io.github.kingschan1204.istock.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.DefaultSpiderImpl;
import io.github.kingschan1204.istock.module.price.po.StockPrice;
import io.github.kingschan1204.istock.module.price.repository.StockPriceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * spider test
 * @author chenguoxiang
 * @create 2018-02-01 10:05
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultSpiderImplTest {
    StockSpider spider=new DefaultSpiderImpl();
    @Autowired
    private StockPriceRepository repository;
    private static final String code="600519";

    @Test
    public void getStockPrice() throws Exception {
       /* JSONObject json =spider.getStockPrice(new String[]{code}).getJSONObject(0);
        StockPrice sp = JSON.parseObject(json.toJSONString() ,StockPrice.class);
        System.out.println(sp.toString());
        StockPrice temp=repository.save(sp);
        System.out.println("save:"+temp.toString());*/

        StockPrice sp =repository.findOne("5a72cb690729c39a34fbb193");
        System.out.println(sp);
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