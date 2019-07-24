package io.github.kingschan1204.istock.module.spider.crawl.report;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.po.StockYearReport;
import io.github.kingschan1204.istock.module.maindata.repository.StockYearReportRepository;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimeJobFactory;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenguoxiang
 * @create 2019-07-24 8:54
 **/
@Slf4j
public class YearReportSpider implements Runnable {

    private AtomicInteger error;
    YearReportSpider(AtomicInteger error){
        this.error=error;
    }

    MongoTemplate getTempleate() {
        return SpringContextUtil.getBean(MongoTemplate.class);
    }

    List<StockCodeInfo> getCode() {
        Integer dateNumber = Integer.valueOf(TradingDateUtil.getDateYYYYMMdd());
        Criteria cr = new Criteria();
        //3天更新一把
        Criteria c1 = Criteria.where("yearReportDate").lt(dateNumber - 3);
        Criteria c2 = Criteria.where("xlsError").is(0);
        Criteria c3 = Criteria.where("yearReportDate").exists(false);
        Query query = new Query(cr.orOperator(c3, new Criteria().andOperator(c1, c2)));
        query.limit(2);
        List<StockCodeInfo> list = getTempleate().find(query, StockCodeInfo.class);
        if (null == list || list.size() == 0) {
            return null;
        }
        return list;
    }

   void doJob()throws Exception{
        List<StockCodeInfo> list = getCode();
        if (null == list) {
            log.info("year report 处理完毕");
            ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.YEAR_REPORT).execute(ITimerJob.COMMAND.STOP);
            return;
        }
        Long start = System.currentTimeMillis();
        list.stream().forEach(code -> {
            JSONArray jsons = null;
            String codestr = code.getCode().replaceAll("\\D", "");
            StockYearReportRepository stockYearReportRepository = SpringContextUtil.getBean(StockYearReportRepository.class);
            Integer dateNumber = Integer.valueOf(TradingDateUtil.getDateYYYYMMdd());
            StockSpider spider = SpringContextUtil.getBean(StockSpider.class);
            try {
                jsons = spider.getHistoryROE(codestr);
                List<StockYearReport> lis = JSON.parseArray(jsons.toJSONString(), StockYearReport.class);
                getTempleate().remove(new Query(Criteria.where("code").is(codestr)), StockYearReport.class);
                stockYearReportRepository.saveAll(lis);
                getTempleate().upsert(
                        new Query(Criteria.where("_id").is(code.getCode())),
                        new Update().set("yearReportDate", dateNumber),
                        "stock_code_info"
                );
            } catch (Exception e) {
                e.printStackTrace();
                getTempleate().upsert(
                        new Query(Criteria.where("_id").is(code.getCode())), new Update().set("xlsError", 1),
                        "stock_code_info"
                );
                error.addAndGet(1);
            }
        });
        log.info(String.format("download xls and update data use ：%s ms ", (System.currentTimeMillis() - start)));
    }

    @Override
    public void run() {
        try {
            doJob();
            if(error.get()>30){
                log.error("错误超过30次，即将关闭YearReport任务");
                ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.YEAR_REPORT).execute(ITimerJob.COMMAND.STOP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
