package io.github.kingschan1204.istock.module.maindata.services;

import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockPriceDaily;
import io.github.kingschan1204.istock.module.spider.openapi.TushareApi;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author chenguoxiang
 * @create 2019-06-11 17:09
 **/
@Slf4j
@Service
public class StockPriceServices {
    @Autowired
    private TradingDateUtil tradingDateUtil;
    @Autowired
    private TushareApi tushareApi;
    @Autowired
    private MongoTemplate template;

    /**
     * 刷新指定日期指定代码的日线数据
     * @param code 代码
     * @param start 开始时间
     * @param end 结束时间
     */
    public void refreshDailyPrice(String code,String start,String end){
        JSONArray data= tushareApi.getStockDailyPrice(TushareApi.formatCode(code),start,end);
        BulkOperations ops = template.bulkOps(BulkOperations.BulkMode.UNORDERED, "stock_price_daily");
        for (int i = 0; i < data.size(); i++) {
            JSONArray row =data.getJSONArray(i);
            /*new Query(Criteria.where("code").is(code)), new Update()
                    .set("code", code)
                    .set("tradeDate", row.get(1))
                    .set("open", row.get(2))
                    .set("close", row.get(5))
                    .set("high", row.get(3))
                    .set("low", row.get(4))
                    .set("change", row.get(7))
                    .set("pct_chg", row.get(8))
                    .set("vol", row.get(9))
                    .set("amount", row.get(10))*/
            StockPriceDaily daily = new StockPriceDaily();
            daily.setCode(code);
            daily.setTradeDate(row.getInteger(1));
            daily.setOpen(row.getDouble(2));
            daily.setClose(row.getDouble(5));
            daily.setHigh(row.getDouble(3));
            daily.setLow(row.getDouble(4));
            daily.setChange(row.getDouble(7));
            daily.setPctchg(row.getDouble(8));
            daily.setVol(row.getDouble(9));
            daily.setAmount(row.getDouble(10));
            ops.insert(daily);
        }
        ops.execute();
    }

    /**
     * 统计日线数据行数
     * @param code
     * @return
     */
    public Long getStockDailyRows(String code){
       return template.count(new Query(Criteria.where("code").is(code)), StockPriceDaily.class);
    }

    /**
     * 得到日线数据
     * @param code
     * @return
     */
    public List<StockPriceDaily> getDailyLine(String code){
        return template.find(new Query(Criteria.where("code").is(code)).with(Sort.by(Sort.Direction.DESC, "tradeDate")),StockPriceDaily.class);
    }
}
