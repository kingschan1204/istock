package io.github.kingschan1204.istock.module.task;

import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeInfoService;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时更新深市股票价格
 *
 * @author chenguoxiang
 * @create 2018-03-29 14:50
 **/
@Component
public class SinaStockPriceTask implements Job {

    private Logger log = LoggerFactory.getLogger(SinaStockPriceTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockCodeInfoService stockCodeInfoService;
    @Autowired
    private StockService stockService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (!StockDateUtil.stockOpenTime()) {
            return;
        }
        Long start = System.currentTimeMillis();
        List<StockCodeInfo> codes = stockCodeInfoService.getSZStockCodes();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            list.add(codes.get(i).getCode());
            if (i > 0 && (i % 300 == 0 || i == codes.size() - 1)) {
                try {
                    stockService.updateStockPrice(list, spider);
                    list = new ArrayList<>();
                    Thread.sleep(800);
                } catch (Exception ex) {
                    log.error("{}", ex);
                    ex.printStackTrace();
                }
            }

        }
        log.info("深市数据更新共：{}只股票,更新耗时：{}ms", codes.size(), (System.currentTimeMillis() - start));

    }


}
