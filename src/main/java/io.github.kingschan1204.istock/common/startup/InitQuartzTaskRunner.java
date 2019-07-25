package io.github.kingschan1204.istock.common.startup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

/**
 * @author chenguoxiang
 * @create 2018-07-13 15:12
 **/
public class InitQuartzTaskRunner implements ApplicationRunner, Ordered {


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {


    }

    @Override
    public int getOrder() {
        return 1;
    }
}
