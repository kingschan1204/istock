package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.WriteResult;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.impl.EastmoneySpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockDividend;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisDividendRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 定时更新分红情况
 *
 * @author chenguoxiang
 * @create 2018-03-29 14:50
 **/
@Component
public class StockDividendTask {

    private Logger log = LoggerFactory.getLogger(StockDividendTask.class);

    @Resource(name = "EastmoneySpider")
    private EastmoneySpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockHisDividendRepository stockHisDividendRepository;

    @Scheduled(cron = "*/6 * * * * ?")
    public void stockDividendExecute() throws Exception {
        /*if (!StockDateUtil.stockOpenTime()) {
            return;
        }*/


        Integer dateNumber = StockDateUtil.getCurrentDateNumber()-5;
        Criteria cr = new Criteria();
        Criteria c1 = Criteria.where("dividendUpdateDay").lt(dateNumber);//小于 （5天更新一遍）
        Criteria c2 = Criteria.where("dividendUpdateDay").exists(false);
        Query query = new Query(cr.orOperator(c1, c2));
        query.limit(3);
        List<Stock> list = template.find(query, Stock.class);
        for (Stock stock:list) {
            Long start =System.currentTimeMillis();//记录开始时间
            int affected=0;//分红更新记录条数
            JSONArray dividends=new JSONArray();
            String date="";
            Double percent=0D;
            try {
                dividends=spider.getHistoryDividendRate(stock.getCode());
                if(null!=dividends&&dividends.size()>0){
                    for (int j = 0; j < dividends.size(); j++) {
                        if(dividends.getJSONObject(j).getDouble("percent")>0){
                            percent=dividends.getJSONObject(j).getDoubleValue("percent");
                            date=dividends.getJSONObject(j).getString("cxcqr");
                            break;
                        }
                    }
                    //save dividend
                    List<StockDividend> stockDividendList = JSONArray.parseArray(dividends.toJSONString(),StockDividend.class);
                    template.remove(new Query(Criteria.where("code").is(stock.getCode())),StockDividend.class);
                    stockHisDividendRepository.save(stockDividendList);
                    affected=stockDividendList.size();
                }
            } catch (Exception e) {
                log.error("error:{}",e);
                e.printStackTrace();
            }
            WriteResult wr =template.upsert(
                    new Query(Criteria.where("_id").is(stock.getCode())),
                    new Update()
                            .set("_id", stock.getCode())
                            .set("dividendDate", date)
                            .set("dividend", percent)
                            .set("dividendUpdateDay", dateNumber),
                    "stock"
            );
            affected+=wr.getN();
            log.info("{}分红抓取,耗时{},获得{}行数据",stock.getCode(),(System.currentTimeMillis()-start),affected);
        }

    }

    public static void main(String[] args) {
        List<String> lis = null;//new ArrayList<>();
        if (Optional.of(lis).isPresent())
        {
            System.out.println("ok");
        }else {
            System.out.println("ng");
        }
    }

}
