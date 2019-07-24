package io.github.kingschan1204.istock.module.spider.crawl.dy;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.client.result.UpdateResult;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockDividend;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisDividendRepository;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimeJobFactory;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author chenguoxiang
 * @create 2019-07-24 15:01
 **/
@Slf4j
public class DividendSpider implements Runnable {

    MongoTemplate getTemplate() {
        return SpringContextUtil.getBean(MongoTemplate.class);
    }

    /**
     * 同花顺和东方财富的并集
     *
     * @param code
     * @return
     * @throws Exception
     */
    public JSONArray combineHisDy(String code) throws Exception {

        String useAgent = SpringContextUtil.getProperties("spider.useagent");

        ThsDividendSpider thsDividendSpider = new ThsDividendSpider(code, useAgent, 3000);
        FutureTask<JSONArray> futureTask = new FutureTask<JSONArray>(thsDividendSpider);
        new Thread(futureTask).start();
        JSONArray ths = futureTask.get(3, TimeUnit.SECONDS);

        EastMoneyDividendSpider eastmoneySpider = new EastMoneyDividendSpider(code, useAgent, 3000);
        FutureTask<JSONArray> futureTask1 = new FutureTask<JSONArray>(eastmoneySpider);
        new Thread(futureTask1).start();
        JSONArray east = futureTask.get(3, TimeUnit.SECONDS);

        if (null == east) {
            east = new JSONArray();
        }
        if (null != ths) {
            east.addAll(ths);
        }
        JSONArray ret = new JSONArray();
        Set<String> titles = new HashSet<String>();
        for (int i = 0; i < east.size(); i++) {
            String title = east.getJSONObject(i).getString("title");
            if (!titles.contains(title)) {
                ret.add(east.getJSONObject(i));
                titles.add(title);
            }
        }
        return ret;
    }

    @Override
    public void run() {
        Integer dateNumber = Integer.valueOf(TradingDateUtil.getDateYYYYMMdd()) - 3;
        Criteria cr = new Criteria();
        //小于 （3天更新一遍）
        Criteria c1 = Criteria.where("dividendUpdateDay").lt(dateNumber);
        Criteria c2 = Criteria.where("dividendUpdateDay").exists(false);
        Query query = new Query(cr.orOperator(c1, c2));
        query.limit(3);
        List<Stock> list = getTemplate().find(query, Stock.class);
        if (null == list || list.size() == 0) {
            log.info("分红率更新完毕！,即将关闭线程!");
            try {
                ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.DIVIDEND).execute(ITimerJob.COMMAND.STOP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        for (Stock stock : list) {
            //记录开始时间
            Long start = System.currentTimeMillis();
            //分红更新记录条数
            int affected = 0;
            JSONArray dividends = new JSONArray();
            String date = "";
            Double percent = 0D;
            try {
                dividends = combineHisDy(stock.getCode());
                if (null != dividends && dividends.size() > 0) {
                    for (int j = 0; j < dividends.size(); j++) {
                        double tempPercent = dividends.getJSONObject(j).getDouble("percent");
                        String tempDate = dividends.getJSONObject(j).getString("cxcqr");
                        if (tempPercent >= 0 && !"-".equals(tempDate)) {
                            percent = tempPercent;
                            date = tempDate;
                            break;
                        }
                    }
                    //save dividend
                    List<StockDividend> stockDividendList = JSONArray.parseArray(dividends.toJSONString(), StockDividend.class);
                    getTemplate().remove(new Query(Criteria.where("code").is(stock.getCode())), StockDividend.class);
                    SpringContextUtil.getBean(StockHisDividendRepository.class).saveAll(stockDividendList);
                    affected = stockDividendList.size();
                }
            } catch (Exception e) {
                log.error("error:{}", e);
                e.printStackTrace();
            }
            UpdateResult updateResult = getTemplate().upsert(
                    new Query(Criteria.where("_id").is(stock.getCode())),
                    new Update()
                            .set("_id", stock.getCode())
                            .set("dividendDate", date)
                            .set("dividend", percent)
                            .set("dividendUpdateDay", dateNumber),
                    "stock"
            );
            affected += updateResult.getModifiedCount();
            log.info("{}分红抓取,耗时{}ms,获得{}行数据", stock.getCode(), (System.currentTimeMillis() - start), affected);
        }
    }
}
