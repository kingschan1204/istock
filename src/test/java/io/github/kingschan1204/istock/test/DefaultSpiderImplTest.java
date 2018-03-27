package io.github.kingschan1204.istock.test;

import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.DefaultSpiderImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

/**
 * spider test
 * @author chenguoxiang
 * @create 2018-02-01 10:05
 **/
//@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultSpiderImplTest {
    StockSpider spider=new DefaultSpiderImpl();
    //@Autowired
    //private StockPriceRepository repository;
    private static final String code="600519";

    @Ignore
    @Test
    public void getStockPrice() throws Exception {
        System.out.println(spider.getStockPrice(new String[]{"600741"}));
    }


    @Ignore
    @Test
    public void getHistoryDividendRate() throws Exception {
        JSONArray jsons =spider.getHistoryDividendRate(code);
        System.out.println(jsons.getJSONObject(0));
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
    public void getHistoryPE() throws Exception {
        System.out.println(spider.getHistoryPE(code).toJSONString());
    }
    @Ignore
    @Test
    public void getHistoryPB() throws Exception {
        System.out.println(spider.getHistoryPB(code).toJSONString());
    }

    @Ignore
    @Test
    public void getAllStockCode()throws Exception{
        Optional<List<String>> optional=Optional.of(spider.getAllStockCode());
        if (optional.isPresent()){
           List<String> codes= optional.get();
            for (String code :
                    codes) {
                String temp=StockSpider.formatStockCode(code.replaceAll("\\D",""));
                if(null==temp||!code.equals(temp)){
                    throw new Exception(code);
                }
                System.out.println(String.format("%s | %s",code,temp));
            }
        }
    }

    @Test
    public  void formatStockCode()throws Exception{

    }

}