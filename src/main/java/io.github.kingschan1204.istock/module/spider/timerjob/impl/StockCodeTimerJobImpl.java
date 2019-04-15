package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeInfoService;
import io.github.kingschan1204.istock.module.maindata.services.StockCompanyService;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import lombok.extern.slf4j.Slf4j;

/**
 * 0点要执行的代码刷新工作
 *
 * @author chenguoxiang
 * @create 2019-03-28 0:27
 **/
@Slf4j
public class StockCodeTimerJobImpl implements ITimerJob {

    @Override
    public void execute(COMMAND command) throws Exception {
        StockCodeInfoService stockCodeInfoService = SpringContextUtil.getBean(StockCodeInfoService.class);
        StockCompanyService stockCompanyService = SpringContextUtil.getBean(StockCompanyService.class);
        Long start = System.currentTimeMillis();
        try {
            stockCodeInfoService.refreshCode();
            stockCompanyService.refreshStockCompany();
        } catch (Exception e) {
            log.error("代码更新错误：{}", e);
            e.printStackTrace();
        }
        log.info(String.format("更新代码共耗时：%s ms", (System.currentTimeMillis() - start)));
    }
}
