package io.github.kingschan1204.istock.task;

import io.github.kingschan1204.istock.common.util.StockDateUtil;
import io.github.kingschan1204.istock.services.StockMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by kingschan on 2017/6/29.
 */
@Component
public class UpdatePriceScheduler {

    private static Logger log = LoggerFactory.getLogger(UpdatePriceScheduler.class);
    @Autowired
    private StockMasterService stockServ;


    @Scheduled(cron="0/4 * * * * ?")
    public void updateStockIPO() throws Exception {
        if(!StockDateUtil.stockOpenTime()){
            if (log.isDebugEnabled()){
                log.debug("现在非开市时间，不更新数据!");
            }
            return ;
        }
        stockServ.updateStockPriceBySina();
    }

}
