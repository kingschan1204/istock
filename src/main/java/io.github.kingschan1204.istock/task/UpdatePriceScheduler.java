package io.github.kingschan1204.istock.task;

import io.github.kingschan1204.istock.common.util.StockOpenDateUtil;
import io.github.kingschan1204.istock.common.util.StockSpilderUtil;
import io.github.kingschan1204.istock.model.dto.SinaStockPriceDto;
import io.github.kingschan1204.istock.repository.StockMasterRepository;
import io.github.kingschan1204.istock.services.StockMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if(!StockOpenDateUtil.stockOpenTime()){
            log.info("现在非开市时间，不更新数据!");
            return ;
        }
        stockServ.updateStockPriceBySina();
    }

}
