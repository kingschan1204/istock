package io.github.kingschan1204.istock.module.spider.crawl.daily;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.po.StockDailyBasic;
import io.github.kingschan1204.istock.module.spider.openapi.TushareApi;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimeJobFactory;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * @author chenguoxiang
 * @create 2019-06-27 10:46
 **/
@Slf4j
public class DailyBasicSpider implements Runnable {
    //当前要处理的代码
    private StockCodeInfo currentCodeInfo;

    public MongoTemplate getMongoTemp() {
        return SpringContextUtil.getBean(MongoTemplate.class);
    }

    /**
     * 得到当前要处理的代码
     *
     * @return
     */
    private void getCodeInfo() {
        Integer dateNumber = Integer.valueOf(TradingDateUtil.getDateYYYYMMdd());
        Criteria cr = new Criteria();
        Criteria c1 = Criteria.where("dailyDate").lt(dateNumber);
        Criteria c2 = Criteria.where("dailyDate").exists(false);
        Query query = new Query(cr.orOperator(c1, c2));
        query.limit(1);
        List<StockCodeInfo> list = getMongoTemp().find(query, StockCodeInfo.class);
        if (null != list && list.size() > 0) {
            this.currentCodeInfo = list.get(0);
        } else {
            try {
                log.info("daily basic 全部更新完毕，关闭更新线程！");
                ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.DAILY_BASIC).execute(ITimerJob.COMMAND.STOP);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("{}", e);
            }
        }
    }

    @Override
    public void run() {
        getCodeInfo();
        if (null == currentCodeInfo) {
            return;
        }
//        TradingDateUtil tradingDateUtil = SpringContextUtil.getBean(TradingDateUtil.class);
        TushareApi tushareApi = SpringContextUtil.getBean(TushareApi.class);
        log.info("daily basic ...{}", currentCodeInfo.getCode());
        String startDate =currentCodeInfo.getList_date().toString();
                //tradingDateUtil.minusDate(3, 0, 0, "yyyyMMdd");
        JSONArray data = tushareApi.getStockDailyBasic(
                TushareApi.formatCode(currentCodeInfo.getCode()),
                startDate, TradingDateUtil.getDateYYYYMMdd());
        DeleteResult deleteResult = null;
        if (data.size() > 0) {
            deleteResult = getMongoTemp().remove(new Query(Criteria.where("code").is(currentCodeInfo.getCode())), "stock_daily_basic");
        }
        BulkOperations ops = getMongoTemp().bulkOps(BulkOperations.BulkMode.UNORDERED, "stock_daily_basic");
        StockDailyBasic dailyBasic;
        for (int i = 0; i < data.size(); i++) {
            JSONArray row = data.getJSONArray(i);
            dailyBasic = new StockDailyBasic();
            dailyBasic.setCode(currentCodeInfo.getCode());
            dailyBasic.setTradeDate(row.getInteger(1));
            dailyBasic.setClose(row.getDouble(2));
            dailyBasic.setPe(row.getDouble(6));
            dailyBasic.setPeTTM(row.getDouble(7));
            dailyBasic.setPb(row.getDouble(8));
            dailyBasic.setTotalShare(row.getDouble(11));
            dailyBasic.setFloatShare(row.getDouble(12));
            dailyBasic.setFreeShare(row.getDouble(13));
            dailyBasic.setTotalMv(row.getDouble(14));
            dailyBasic.setCircMv(row.getDouble(15));
            ops.insert(dailyBasic);
        }
        BulkWriteResult bulkWriteResult = ops.execute();
        UpdateResult updateResult2 = getMongoTemp().upsert(
                new Query(Criteria.where("_id").is(currentCodeInfo.getCode())),
                new Update().set("dailyDate", Integer.valueOf(TradingDateUtil.getDateYYYYMMdd())), "stock_code_info");
        log.info("insert: {},update: {} delete: {}", bulkWriteResult.getInsertedCount(), updateResult2.getModifiedCount(), deleteResult.getDeletedCount());
    }


}
