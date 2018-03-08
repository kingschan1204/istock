package io.github.kingschan1204.istock.test;

import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.DefaultSpiderImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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


        /*StockPrice sp =repository.findOne("5a72cb690729c39a34fbb193");
        System.out.println(sp);*/
    }

    @Ignore
    @Test
    public void getStockInfo() throws Exception {
        System.out.println(spider.getStockInfo(code).toJSONString());
    }
    @Ignore
    @Test
    public void getHistoryDividendRate() throws Exception {
        System.out.println(spider.getHistoryDividendRate(code).toJSONString());
    }
    @Ignore
    @Test
    public void getHistoryROE() throws Exception {
        System.out.println(spider.getHistoryROE(code));
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

    @Ignore
    @Test
    public  void formatStockCode(){
        String code =StockSpider.formatStockCode("300314");
        System.out.println(code);
    }

}