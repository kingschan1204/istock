package io.github.kingschan1204.istock.test;

import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.DefaultSpiderImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * spider test
 * @author chenguoxiang
 * @create 2018-02-01 10:05
 **/
@WebAppConfiguration
//@PropertySource(value = { "classpath:application.properties" })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DefaultSpiderImplTest {
    StockSpider spider=new DefaultSpiderImpl();
    //@Autowired
    //private StockPriceRepository repository;
    private static final String code="600519";

    @Ignore
    @Test
    public void getStockPrice() throws Exception {
        List<String> codes=spider.getAllStockCode();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            list.add(codes.get(i));
            if(i>0&&(i%300==0||i==codes.size()-1)){
                System.out.println(spider.getStockPrice(list.toArray(new String[]{})));
                list=new ArrayList<>();
                Thread.sleep(1000);
            }

        }

    }
    @Test
    public void getStockInfo() throws Exception {
        System.out.println(spider.getStockInfo("600519"));
    }


    @Test
    public void getHistoryDividendRate() throws Exception {
        JSONArray jsons =spider.getHistoryDividendRate("000550");
        System.out.println(jsons);
    }
    @Ignore
    @Test
    public void getHistoryROE() throws Exception {
        JSONArray jsons =spider.getHistoryROE(code);
        System.out.println(jsons.getJSONObject(0));
        System.out.println(jsons);
    }

    @Ignore
    @Test
    public void getAllStockCode()throws Exception{
        Optional<List<String>> optional=Optional.of(spider.getAllStockCode());
        if (optional.isPresent()){
           List<String> codes= optional.get();
            for (String code :
                    codes) {
                System.out.println(code);
            }
        }
    }

    @Test
    public  void getdy()throws Exception{
       System.out.println(spider.getDy(1));

    }

}