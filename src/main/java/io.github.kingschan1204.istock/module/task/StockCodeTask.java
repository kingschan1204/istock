package io.github.kingschan1204.istock.module.task;

import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class StockCodeTask {

    private Logger log = LoggerFactory.getLogger(StockCodeTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockCodeService stockCodeService;

//    @Scheduled(cron = "0 0 0 * * ?")
    public void stockCodeExecute()throws Exception{
        Long start =System.currentTimeMillis();
        stockCodeService.saveAllStockCode();
        log.info(String.format("更新代码共耗时：%s ms",(System.currentTimeMillis()-start)));
    }
}
