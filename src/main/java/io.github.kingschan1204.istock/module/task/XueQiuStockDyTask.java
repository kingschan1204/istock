package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.WriteResult;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockDyQueue;
import io.github.kingschan1204.istock.module.maindata.repository.StockDyQueueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时更新分红情况
 *
 * @author chenguoxiang
 * @create 2018-03-29 14:50
 **/
@Component
public class XueQiuStockDyTask {

    private Logger log = LoggerFactory.getLogger(XueQiuStockDyTask.class);

    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockDyQueueRepository stockDyQueueRepository;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void stockDividendExecute() throws Exception {
        int day=StockDateUtil.getCurrentWeekDay();
        if(day==6||day==0){
            log.info("非交易时间不执行操作...");
            return ;
        }
        log.info("开始更新stock dy 数据");
        Long start =System.currentTimeMillis();
       List<StockDyQueue> list= template.find(
                new Query(Criteria.where("date").is(StockDateUtil.getCurrentDateNumber())), StockDyQueue.class
        );
       int pageindex=1;
       int totalpage=25;
       if(null!=list&&list.size()>0){
            pageindex=list.size()+1;
            //totalpage=list.get(0).getTotalPage();
       }
       if(pageindex>totalpage){
           log.info("stock dy 已经全部更新完");
           return ;
       }
        try {
           log.info("dy开始更新第{}页",pageindex);
            JSONObject data =spider.getDy(pageindex);
            uptateDy(data);

            int total=data.getInteger("count");
            int pagesize=total%100==0?total/100:total/100+1;

            StockDyQueue stockDyQueue = new StockDyQueue();
            stockDyQueue.setDate(StockDateUtil.getCurrentDateNumber());
            stockDyQueue.setPageIndex(pageindex);
            stockDyQueue.setTotalPage(pagesize);
            template.save(stockDyQueue,"stock_dy_queue");
        }catch (Exception ex){
           ex.printStackTrace();
           log.error("dy 出错了:{}",ex);
        }
        log.info(String.format("dy更新一批耗时：%s ms",(System.currentTimeMillis()-start)));
    }

    public void uptateDy(JSONObject data){
        Integer dateNumber = StockDateUtil.getCurrentDateNumber();
        JSONArray rows = data.getJSONArray("list");
        List<Stock> list =rows.toJavaList(Stock.class);
        list.stream().forEach(stock -> {
            WriteResult wr =template.updateFirst(
                    new Query(Criteria.where("_id").is(stock.getCode())),
                    new Update()
                            .set("dy", stock.getDy())
                            .set("dyDate", dateNumber),
                    "stock"
            );
            log.info("dy更新受影响行："+wr.getN());
        });
    }
}
