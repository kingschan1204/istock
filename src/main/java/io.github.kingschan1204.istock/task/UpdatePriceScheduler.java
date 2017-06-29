package io.github.kingschan1204.istock.task;

import io.github.kingschan1204.istock.common.util.StockUtil;
import io.github.kingschan1204.istock.model.dto.SinaStockPriceDto;
import io.github.kingschan1204.istock.model.dto.StockMasterDto;
import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import io.github.kingschan1204.istock.repository.StockMasterRepository;
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
    private StockMasterRepository stockRepository;

    @Transactional
    @Scheduled(cron="0/4 * * * * ?")
    public void updateStockIPO() throws Exception {
        log.info("update sina stock ipo");
        String[] codes = stockRepository.getAllStockCode();
        List<SinaStockPriceDto> stocks= StockUtil.getStockPrice(codes);
        for (SinaStockPriceDto dto :stocks) {
            /*log.info("{} {} {} {} {}",
                    dto.getsStockName(),
                    dto.getsCurrentPrice().doubleValue(),
                    dto.getsYesterdayPrice().doubleValue(),
                    dto.getsRangePrice().doubleValue(),
                    dto.getsCode());*/
            stockRepository.updateIPO(
                    dto.getsStockName(),
                    dto.getsCurrentPrice(),
                    dto.getsYesterdayPrice(),
                    dto.getsRangePrice(),
                    dto.getsCode());
        }
    }

}
