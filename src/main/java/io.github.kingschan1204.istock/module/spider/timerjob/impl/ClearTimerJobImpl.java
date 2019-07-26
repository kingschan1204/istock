package io.github.kingschan1204.istock.module.spider.timerjob.impl;

import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.maindata.po.StockHisPbPe;
import io.github.kingschan1204.istock.module.maindata.po.StockPriceDaily;
import io.github.kingschan1204.istock.module.maindata.po.StockReport;
import io.github.kingschan1204.istock.module.spider.timerjob.AbstractTimeJob;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.util.Arrays;

/**
 * 0点要执行的清理工作
 * @author chenguoxiang
 * @create 2019-03-28 0:27
 **/
@Slf4j
public class ClearTimerJobImpl extends AbstractTimeJob {

   public ClearTimerJobImpl(){
        name="清理任务";
    }

    @Override
    public void execute(COMMAND command) throws Exception {
        log.info("执行清理工作...");
        MongoTemplate mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
        File f = new File("./data/");
        if(!f.exists()){return;}
        Arrays.stream(f.listFiles()).forEach(file ->{
            log.info("清理文件:{} {}", file.getName(),file.delete());
        });

        mongoTemplate.dropCollection(StockHisPbPe.class);
        mongoTemplate.dropCollection(StockReport.class);
        mongoTemplate.dropCollection(StockPriceDaily.class);
        log.info("{}","删除历史pb,pe,price,报表数据");
    }
}
