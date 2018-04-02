package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.WriteResult;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockHisDividend;
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

import java.util.List;

/**
 * 定时更新分红情况
 *
 * @author chenguoxiang
 * @create 2018-03-29 14:50
 **/
@Component
public class ThsStockDividendTask {

    private Logger log = LoggerFactory.getLogger(ThsStockDividendTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockHisDividendRepository stockHisDividendRepository;

    @Scheduled(cron = "*/6 * * * * ?")
    public void stockDividendExecute() throws Exception {
        if(!StockDateUtil.stockOpenTime()){
            log.info("非交易时间不执行操作...");
            return ;
        }
        log.info("开始更新stock Dividend 数据");
        Long start =System.currentTimeMillis();
        Integer dateNumber = StockDateUtil.getCurrentDateNumber()-5;
        Criteria cr = new Criteria();
        Criteria c1 = Criteria.where("dividendUpdateDay").lt(dateNumber);
        Criteria c2 = Criteria.where("dividendUpdateDay").exists(false);
        Query query = new Query(cr.orOperator(c1, c2));
        query.limit(3);
        List<Stock> list = template.find(query, Stock.class);
        list.stream().forEach(stock -> {
            JSONArray dividends=new JSONArray();
            String date="";
            Double percent=0D;
            try {
                 dividends=spider.getHistoryDividendRate(stock.getCode());
                if(null!=dividends&&dividends.size()>0){
                    for (int j = 0; j < dividends.size(); j++) {
                        if(dividends.getJSONObject(j).getDouble("percent")>0){
                            percent=dividends.getJSONObject(j).getDoubleValue("percent");
                            date=dividends.getJSONObject(j).getString("date");
                            break;
                        }
                    }
                    //save dividend
                    List<StockHisDividend> stockHisDividendList = JSONArray.parseArray(dividends.toJSONString(),StockHisDividend.class);
                    template.remove(new Query(Criteria.where("code").is(stock.getCode())),StockHisDividend.class);
                    stockHisDividendRepository.save(stockHisDividendList);
                }
            } catch (Exception e) {
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
            log.info("dividend更新受影响行："+wr.getN());
        });
        log.info(String.format("dividend更新一批耗时：%s ms",(System.currentTimeMillis()-start)));
    }
}
