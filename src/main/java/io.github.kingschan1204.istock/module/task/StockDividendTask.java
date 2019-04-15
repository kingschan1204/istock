package io.github.kingschan1204.istock.module.task;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.WriteResult;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.impl.DefaultSpiderImpl;
import io.github.kingschan1204.istock.common.util.stock.impl.EastmoneySpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockDividend;
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
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 定时更新分红情况
 *
 * @author chenguoxiang
 * @create 2018-03-29 14:50
 **/
@Component
public class StockDividendTask implements Job {

    private Logger log = LoggerFactory.getLogger(StockDividendTask.class);

    @Resource(name = "EastmoneySpider")
    private EastmoneySpider eastmoneySpider;
    @Autowired
    private DefaultSpiderImpl defaultSpider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private StockHisDividendRepository stockHisDividendRepository;

    /**
     * 同花顺和东方财富的并集
     *
     * @param code
     * @return
     * @throws Exception
     */
    public JSONArray combineHisDy(String code) throws Exception {
        JSONArray ths = defaultSpider.getHistoryDividendRate(code);
        JSONArray east = eastmoneySpider.getHistoryDividendRate(code);
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
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Integer dateNumber = StockDateUtil.getCurrentDateNumber() - 3;
        Criteria cr = new Criteria();
        //小于 （3天更新一遍）
        Criteria c1 = Criteria.where("dividendUpdateDay").lt(dateNumber);
        Criteria c2 = Criteria.where("dividendUpdateDay").exists(false);
        Query query = new Query(cr.orOperator(c1, c2));
        query.limit(3);
        List<Stock> list = template.find(query, Stock.class);
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
                    template.remove(new Query(Criteria.where("code").is(stock.getCode())), StockDividend.class);
                    stockHisDividendRepository.save(stockDividendList);
                    affected = stockDividendList.size();
                }
            } catch (Exception e) {
                log.error("error:{}", e);
                e.printStackTrace();
            }
            WriteResult wr = template.upsert(
                    new Query(Criteria.where("_id").is(stock.getCode())),
                    new Update()
                            .set("_id", stock.getCode())
                            .set("dividendDate", date)
                            .set("dividend", percent)
                            .set("dividendUpdateDay", dateNumber),
                    "stock"
            );
            affected += wr.getN();
            log.info("{}分红抓取,耗时{}ms,获得{}行数据", stock.getCode(), (System.currentTimeMillis() - start), affected);
        }

    }
}
