package io.github.kingschan1204.istock.test;

import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.DefaultSpiderImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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
                System.out.println(code);
            }
        }
    }

    @Test
    public  void getStockInfo()throws Exception{
        System.out.println(spider.getStockInfo("000995"));
    }

}