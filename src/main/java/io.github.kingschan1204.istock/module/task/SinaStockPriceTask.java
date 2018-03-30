package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时更新股票价格
 * @author chenguoxiang
 * @create 2018-03-29 14:50
 **/
@Component
public class SinaStockPriceTask {

    private Logger log = LoggerFactory.getLogger(SinaStockPriceTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;

    @Scheduled(cron = "0/20 * * * * ?")
    public void stockPriceExecute()throws Exception{
        if(!StockDateUtil.stockOpenTime()){
            log.info("非交易时间不执行操作...");
            return ;
        }
        log.info("开始更新stock主数据");
        List<String> codes=spider.getAllStockCode();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            list.add(codes.get(i));
            if(i>0&&(i%300==0||i==codes.size()-1)){
                //
                JSONArray jsons = spider.getStockPrice(list.toArray(new String[]{}));
                List<Stock> stocks = JSON.parseArray(jsons.toJSONString(), Stock.class);
                stocks.stream().forEach(stock->{
                    template.upsert(
                            new Query(Criteria.where("_id").is(stock.getCode())),
                            new Update()
                            .set("_id",stock.getCode())
                            .set("type",stock.getType())
                            .set("name",stock.getName())
                            .set("price",stock.getPrice())
                            .set("yesterdayPrice",stock.getYesterdayPrice())
                            .set("fluctuate",stock.getFluctuate())
                            .set("todayMax",stock.getTodayMax())
                            .set("todayMin",stock.getTodayMin())
                            .set("priceDate",stock.getPriceDate()),
                            "stock"
                    );
                });
                //
                list=new ArrayList<>();
                Thread.sleep(800);
            }

        }
    }
}
