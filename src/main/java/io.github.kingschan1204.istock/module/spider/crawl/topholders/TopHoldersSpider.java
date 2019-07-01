package io.github.kingschan1204.istock.module.spider.crawl.topholders;

import com.mongodb.client.result.UpdateResult;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.services.StockTopHoldersService;
import io.github.kingschan1204.istock.module.spider.openapi.TushareApi;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimeJobFactory;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.util.List;

/**
 * top 10 holders spider
 * @author chenguoxiang
 * @create 2019-03-28 17:58
 **/
@Slf4j
public class TopHoldersSpider implements Runnable{

    /**
     * 获取作业code
     * @param template
     * @return
     */
    String getCode(MongoTemplate template) throws Exception {
        //3天更新一遍
        Integer dateNumber = Integer.valueOf(TradingDateUtil.minusDate(0,0,3,"yyyyMMdd"));
        Criteria cr = new Criteria();
        Criteria c1 = Criteria.where("holdersDate").lt(dateNumber);
        Criteria c2 = Criteria.where("holdersDate").exists(false);
        Query query = new Query(cr.orOperator(c1,c2));
        query.limit(1);
        List<StockCodeInfo> list = template.find(query, StockCodeInfo.class);
        if(null!=list&&list.size()>0){
            return list.get(0).getCode();
        }
        log.info("top holder 更新完毕！关闭作业");
        ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.TOP_HOLDER).execute(ITimerJob.COMMAND.STOP);
        return null;
    }

    @Override
    public void run() {
        try {
            MongoTemplate mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
            StockTopHoldersService stockTopHoldersService=SpringContextUtil.getBean(StockTopHoldersService.class);
            String code = getCode(mongoTemplate);
            if(null==code){
                return;
            }
            stockTopHoldersService.refreshTopHolders(TushareApi.formatCode(code));
           UpdateResult updateResult= mongoTemplate.upsert(
                    new Query(Criteria.where("_id").is(code)),
                    new Update().set("holdersDate",Integer.valueOf(TradingDateUtil.getDateYYYYMMdd())),"stock_code_info");
           log.info("代码{}top holders 更新{}行",code,updateResult.getMatchedCount());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}",e);
        }
    }
}
