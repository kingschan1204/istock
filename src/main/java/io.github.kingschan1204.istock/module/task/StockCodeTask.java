package io.github.kingschan1204.istock.module.task;

import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeInfoService;
import io.github.kingschan1204.istock.module.maindata.services.StockCompanyService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * 代码定时更新任务
 * @author kings.chan
 */
@Component
public class StockCodeTask implements Job{

    private Logger log = LoggerFactory.getLogger(StockCodeTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockCodeInfoService stockCodeInfoService;
    @Autowired
    private StockCompanyService stockCompanyService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Long start =System.currentTimeMillis();
        try {
            stockCodeInfoService.refreshCode();
            stockCompanyService.refreshStockCompany();
        } catch (Exception e) {
            log.error("代码更新错误：{}",e);
            e.printStackTrace();
        }
        log.info(String.format("更新代码共耗时：%s ms",(System.currentTimeMillis()-start)));
    }
}
