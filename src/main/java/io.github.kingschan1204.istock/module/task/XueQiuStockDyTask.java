package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.WriteResult;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
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

    @Scheduled(cron = "0 0/10 * * * ?")
    public void stockDividendExecute() throws Exception {
       /* if (true){
            return ;
        }*/
        log.info("开始更新stock dy 数据");
        Long start =System.currentTimeMillis();
        JSONObject data =spider.getDy(1);
        int total=data.getInteger("count");
        int pagesize=total%30==0?total/30:total/30+1;
        int pageindex=1;
        do{
            if(pageindex>1){
                data =spider.getDy(pageindex);
            }
            log.info("dy 更新页：{}",pageindex);
            uptateDy(data);
            pageindex++;
        }while (pageindex<=pagesize);
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
