package io.github.kingschan1204.istock.module.startup;

import io.github.kingschan1204.istock.common.util.quartz.QuartzManager;
import io.github.kingschan1204.istock.module.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

/**
 *
 * @author chenguoxiang
 * @create 2018-07-13 15:12
 **/
public class InitQuartzTaskRunner implements ApplicationRunner, Ordered {

    @Autowired
    private QuartzManager quartzManager;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        quartzManager.addJob("sinaPriceTask",
                "sinaPriceTask-group",
                "sinaPriceTask-trigger",
                "sinaPriceTask-trigger-group",
                SinaStockPriceTask.class,
                "0/30 * * * * ?");

        quartzManager.addJob("tencentPriceTask",
                "tencentPriceTask-group",
                "tencentPriceTask-trigger",
                "tencentPriceTask-trigger-group",
                TencentStockPriceTask.class,
                "0/30 * * * * ?");

        quartzManager.addJob("stockCodeTask",
                "stockCodeTask-group",
                "stockCodeTask-trigger",
                "stockCodeTask-trigger-group",
                StockCodeTask.class,
                "0 0 0 * * ?");

        quartzManager.addJob("stockDividendTask",
                "stockDividendTask-group",
                "stockDividendTask-trigger",
                "stockDividendTask-trigger-group",
                StockDividendTask.class,
                "6 * * * * ?");


        quartzManager.addJob("hisRepoartTask",
                "hisRepoartTask-group",
                "hisRepoartTask-trigger",
                "hisRepoartTask-trigger-group",
                ThsHisYearReportTask.class,
                "6 * * * * ?");

        quartzManager.addJob("stockInfoTask",
                "stockInfoTask-group",
                "stockInfoTask-trigger",
                "stockInfoTask-trigger-group",
                ThsStockInfoTask.class,
                "6 * * * * ?");

        quartzManager.addJob("xueqiuDyTask",
                "xueqiuDyTask-group",
                "xueqiuDyTask-trigger",
                "xueqiuDyTask-trigger-group",
                XueQiuStockDyTask.class,
                "0 0/1 * * * ?");

        quartzManager.addJob("CleanFileTask",
                "CleanFileTask-group",
                "CleanFileTask-trigger",
                "CleanFileTask-trigger-group",
                CleanFileTask.class,
                "0 0 0 * * ?");
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
