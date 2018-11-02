package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.WriteResult;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisDividendRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * 定时更新股票信息
 *
 * @author chenguoxiang
 * @create 2018-03-29 14:50
 **/
@Component
public class ThsStockInfoTask implements Job{

    private Logger log = LoggerFactory.getLogger(ThsStockInfoTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockHisDividendRepository stockHisDividendRepository;

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
        Criteria c1 = Criteria.where("infoDate").lt(dateNumber);
        Criteria c2 = Criteria.where("infoDate").exists(false);
        Query query = new Query(cr.orOperator(c1,c2));
        query.limit(4);
        List<StockCodeInfo> list = template.find(query, StockCodeInfo.class);
        if(null==list||list.size()==0){
            return ;
        }
        int affected=0;
        for (StockCodeInfo stock :list) {
            Stock item = null;
            try {
                JSONObject info = spider.getStockInfo(stock.getCode());
                item = info.toJavaObject(Stock.class);
                if (null == item) {return;}
                WriteResult wr = template.upsert(
                        new Query(Criteria.where("_id").is(stock.getCode())),
                        new Update()
                                .set("_id", stock.getCode())
                                .set("industry", item.getIndustry())
                                .set("mainBusiness", item.getMainBusiness())
                                .set("totalValue", item.getTotalValue())
                                .set("pb", item.getPb())
                                .set("roe", item.getRoe())
                                .set("bvps", item.getBvps())
                                .set("pes", item.getPes())
                                .set("ped", item.getPed()),
                                //,
                        "stock"
                );
                template.upsert(
                        new Query(Criteria.where("_id").is(stock.getCode())),
                        new Update().set("infoDate", item.getInfoDate()),"stock_code_info");
                affected+=wr.getN();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("{}",e);
                ;
            }
        }
        log.info(String.format("craw stock info and update data use ：%s ms ,affected rows : %s", (System.currentTimeMillis() - start),affected));
    }
}
