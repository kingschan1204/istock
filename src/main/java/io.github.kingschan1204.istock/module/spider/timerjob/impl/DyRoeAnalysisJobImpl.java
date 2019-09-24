package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * @author chenguoxiang
 * @create 2019-09-24 15:26
 **/
@Slf4j
public class DyRoeAnalysisJobImpl extends AbstractTimeJob {

   public DyRoeAnalysisJobImpl(){
        name="ROE,DY计算任务";
    }
    @Override
    public void execute(COMMAND command) throws Exception {
        StockService stockService = SpringContextUtil.getBean(StockService.class);
        LocalDate localDate = LocalDate.now();
        //3年 dy
        int start_year_3 = localDate.getYear() - 3;
        log.info("3年dy计算：{}-{}-{}", start_year_3, localDate.getYear(), "threeYearDy");
        stockService.calculateFiveYearsDy(start_year_3, localDate.getYear(), "threeYearDy");
        //5 dy
        int start_year_5 = localDate.getYear() - 5;
        stockService.calculateFiveYearsDy(start_year_5, localDate.getYear(), "fiveYearDy");
        log.info("5年dy计算：{}-{}-{}", start_year_5, localDate.getYear(), "fiveYearDy");

        log.info("3年ROE计算：{}-{}-{}", start_year_3, localDate.getYear(), "threeYearRoe");
        stockService.calculateFiveYearsRoe(start_year_3, localDate.getYear(), "threeYearRoe");

        stockService.calculateFiveYearsRoe(start_year_5, localDate.getYear(), "fiveYearRoe");
        log.info("5年ROE计算：{}-{}-{}", start_year_5, localDate.getYear(), "fiveYearRoe");
    }
}
