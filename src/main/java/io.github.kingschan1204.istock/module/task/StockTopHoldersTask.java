package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.WriteResult;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.TushareSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisDividendRepository;
import io.github.kingschan1204.istock.module.maindata.services.StockTopHoldersService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时更新前10名持有人信息
 *
 * @author chenguoxiang
 * @create 2018-11-1 14:50
 **/
@Component
public class StockTopHoldersTask implements Job{

    private Logger log = LoggerFactory.getLogger(StockTopHoldersTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockTopHoldersService stockTopHoldersService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /*try {
            if (!StockSpider.isWorkDay(StockDateUtil.getCurrentDateNumber())) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Long start = System.currentTimeMillis();
        Integer dateNumber = StockDateUtil.getCurrentDateNumber();
        Criteria cr = new Criteria();
        Criteria c1 = Criteria.where("holdersDate").lt(dateNumber);
        Criteria c2 = Criteria.where("holdersDate").exists(false);
        Query query = new Query(cr.orOperator(c1,c2));
        query.limit(4);
        List<StockCodeInfo> list = template.find(query, StockCodeInfo.class);
        if(null==list||list.size()==0){
            return ;
        }
        for (StockCodeInfo stock :list) {
            try {
                stockTopHoldersService.refreshTopHolders(TushareSpider.formatCode(stock.getCode()));
                template.upsert(
                        new Query(Criteria.where("code").is(stock.getCode())),
                        new Update().set("holdersDate",StockDateUtil.getCurrentDateNumber()),"stock_code_info");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("{}",e);
                ;
            }
        }
        log.info(String.format("update stock top 10 holders use  ：%s ms ", (System.currentTimeMillis() - start)));
    }
}
