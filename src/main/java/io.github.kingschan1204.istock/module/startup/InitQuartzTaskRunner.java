package io.github.kingschan1204.istock.module.startup;

import io.github.kingschan1204.istock.common.util.quartz.QuartzManager;
import io.github.kingschan1204.istock.module.task.StockDividendTask;
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
        quartzManager.addJob("dyTask",
                "dyTask-group",
                "dyTask-trigger",
                "dyTask-trigger-group",
                StockDividendTask.class,
                "*/6 * * * * ?");
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
