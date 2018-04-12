package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.DefaultSpiderImpl;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockCode;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeService;
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

/**
 * 定时更新股票价格
 *
 * @author chenguoxiang
 * @create 2018-03-29 14:50
 **/
@Component
public class SinaStockPriceTask {

    private Logger log = LoggerFactory.getLogger(SinaStockPriceTask.class);

    @Resource(name = "DefaultSpiderImpl")
    private DefaultSpiderImpl spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockCodeService stockCodeService;

    @Scheduled(cron = "0/20 * * * * ?")
    public void stockPriceExecute() throws Exception {
        if (!StockDateUtil.stockOpenTime()) {
            return;
        }
        Long start = System.currentTimeMillis();
        List<StockCode> codes = stockCodeService.getAllStockCodes();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            list.add(codes.get(i).getCode());
            if (i > 0 && (i % 300 == 0 || i == codes.size() - 1)) {
                try {
                    updateStockPrice(list);
                    list = new ArrayList<>();
                    log.info(String.format("stock更新耗时：%s ms", (System.currentTimeMillis() - start)));
                    Thread.sleep(800);
                } catch (Exception ex) {
                    log.error("{}", ex);
                    ex.printStackTrace();
                }
            }

        }


    }


    public void updateStockPrice(List<String> codes) throws Exception {
        JSONArray jsons = spider.getStockPrice(codes.toArray(new String[]{}));
        List<Stock> stocks = JSON.parseArray(jsons.toJSONString(), Stock.class);
        stocks.stream().forEach(stock -> {
            template.upsert(
                    new Query(Criteria.where("_id").is(stock.getCode())),
                    new Update()
                            .set("_id", stock.getCode())
                            .set("type", stock.getType())
                            .set("name", stock.getName())
                            .set("price", stock.getPrice())
                            .set("yesterdayPrice", stock.getYesterdayPrice())
                            .set("fluctuate", stock.getFluctuate())
                            .set("todayMax", stock.getTodayMax())
                            .set("todayMin", stock.getTodayMin())
                            .set("priceDate", stock.getPriceDate()),
                    "stock"
            );
        });
    }
}
