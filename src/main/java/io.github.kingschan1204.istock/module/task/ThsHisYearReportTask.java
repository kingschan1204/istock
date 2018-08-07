package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.cache.EhcacheUtil;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.StockCode;
import io.github.kingschan1204.istock.module.maindata.po.StockHisRoe;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisRoeRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬取历史roe
 * @author chenguoxiang
 * @create 2018-07-09 17:28
 **/
@Component
public class ThsHisYearReportTask implements Job{
    private Logger log = LoggerFactory.getLogger(ThsHisYearReportTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockHisRoeRepository stockHisRoeRepository;
    @Autowired
    EhcacheUtil ehcacheUtil;
    final String cacheName="ThsHisYearReportTask";

    /**
     * 是否错误次数过多，停止任务
     * @return
     */
    boolean stopTask(){
        return getErrorTotal()>3;
    }

    /**
     * 得到执行错误的记录
     * @return
     */
    int getErrorTotal(){
        Object value =ehcacheUtil.getKey(cacheName,"error");
        int val=null==value?0:(int)value;
        return val;
    }


//    @Scheduled(cron = "*/6 * * * * ?")
  /*  public void execute() throws Exception {
    }*/

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Long start = System.currentTimeMillis();
        if(stopTask()){
            log.info("错误次数过多，不执行任务!");
            return;
        }
        Integer dateNumber = StockDateUtil.getCurrentDateNumber();
        Criteria cr = new Criteria();
        //3天更新一把
        Criteria c1 = Criteria.where("hrdud").lt(dateNumber-3);
        Criteria c2 = Criteria.where("xlsError").is(0);
        Query query = new Query(cr.andOperator(c1,c2));
        query.limit(2);
        List<StockCode> list = template.find(query, StockCode.class);
        if(null==list||list.size()==0){
            log.info("his year report data 当前已全部更新完!");
            return ;
        }
        list.stream().forEach(code ->{
            JSONArray jsons= null;
            String codestr=code.getCode().replaceAll("\\D","");
            try {
                jsons = spider.getHistoryROE(codestr);
                List<StockHisRoe> lis = JSON.parseArray(jsons.toJSONString(),StockHisRoe.class);
                template.remove(new Query(Criteria.where("code").is(codestr)),StockHisRoe.class);
                stockHisRoeRepository.save(lis);
                template.upsert(
                        new Query(Criteria.where("_id").is(code.getCode())),
                        new Update().set("hrdud", dateNumber),
                        "stock_code"
                );
            } catch (Exception e) {
                e.printStackTrace();
                ehcacheUtil.addKey(cacheName,"error",getErrorTotal()+1);
                template.upsert(
                        new Query(Criteria.where("_id").is(code.getCode())),new Update().set("xlsError", 1),
                        "stock_code"
                );
            }
        });
        log.info(String.format("download xls and update data use ：%s ms ", (System.currentTimeMillis() - start)));
        //end
    }
}
