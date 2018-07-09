package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.StockCode;
import io.github.kingschan1204.istock.module.maindata.po.StockHisRoe;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisRoeRepository;
import io.github.kingschan1204.istock.module.maindata.services.StockHisRoeService;
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
 * ${DESCRIPTION}
 *
 * @author chenguoxiang
 * @create 2018-07-09 17:28
 **/
@Component
public class ThsHisYearReportTask {
    private Logger log = LoggerFactory.getLogger(ThsHisYearReportTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockHisRoeRepository stockHisRoeRepository;

    @Scheduled(cron = "*/6 * * * * ?")
    public void execute() throws Exception {
        if (StockDateUtil.stockOpenTime()) { //开盘时间不处理
            return;
        }
        Integer dateNumber = StockDateUtil.getCurrentDateNumber();
        Criteria cr = new Criteria();
        Criteria c1 = Criteria.where("hrdud").lt(dateNumber-3); //3天更新一把
        Criteria c2 = Criteria.where("hrdud").exists(false);
        Query query = new Query(cr.orOperator(c1,c2));
        List<Sort.Order> orders = new ArrayList<Sort.Order>();  //排序
        orders.add(new Sort.Order(Sort.Direction.ASC,"_id"));
        Sort sort = new Sort(orders);
        query.with(sort);
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
            }
        });

    }
}
