package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.cache.EhcacheUtil;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.po.StockHisRoe;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisRoeRepository;
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
        Criteria c1 = Criteria.where("yearReportDate").lt(dateNumber-3);
        Criteria c2 = Criteria.where("xlsError").is(0);
        Criteria c3 = Criteria.where("yearReportDate").exists(false);
        Query query = new Query(cr.orOperator(c3,new Criteria().andOperator(c1,c2)));
        query.limit(2);
        List<StockCodeInfo> list = template.find(query, StockCodeInfo.class);
        if(null==list||list.size()==0){
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
                        new Update().set("yearReportDate", dateNumber),
                        "stock_code_info"
                );
            } catch (Exception e) {
                e.printStackTrace();
                ehcacheUtil.addKey(cacheName,"error",getErrorTotal()+1);
                template.upsert(
                        new Query(Criteria.where("_id").is(code.getCode())),new Update().set("xlsError", 1),
                        "stock_code_info"
                );
            }
        });
        log.info(String.format("download xls and update data use ：%s ms ", (System.currentTimeMillis() - start)));
        //end
    }
}
