package io.github.kingschan1204.istock.module.maindata.ctrl;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeService;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

/**
 * @author chenguoxiang
 * @create 2018-03-27 11:14
 **/
@RestController
public class StockCtrl {

    @Autowired
    private StockService service;
    @Autowired
    private StockCodeService stockCodeService;
    @Autowired
    private StockSpider spider;

    @RequestMapping("/stock/q")
    public String queryStock(Integer page, Integer rows, String code, String type, String pb, String dy, String sidx, String sord) {
        String order = (null == sord || sord.isEmpty()) ? "asc" : sord;
        String field = (null == sidx || sidx.isEmpty()) ? "_id" : sidx;
        return service.queryStock(page, rows, code, type, pb, dy, field, order);
    }


    @ResponseBody
    @RequestMapping(value = "/stock/init_code", method = RequestMethod.POST)
    public String initCode() {
        try {
            stockCodeService.saveAllStockCode();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Autowired
    private MongoTemplate template;

    @ResponseBody
    @RequestMapping(value = "/stock/mapReduce/5years_dy")
    public String fiveYearsDy() {
        int year = StockDateUtil.getCurrentYear();
        int fiveYearAgo = year - 5;
        String startDate = StockDateUtil.getCurrYearLastDay(fiveYearAgo).toString();
        String endDate = StockDateUtil.getCurrentDate();
        Query query = new Query();
        query.addCriteria(Criteria.where("releaseDate").gte(startDate).lte(endDate));
        MapReduceResults<BasicDBObject> result = template.mapReduce(query, "stock_dividend",
                "classpath:/mapreduce/5years_dy/dy5years_map.js", "classpath:/mapreduce/5years_dy/dy5years_reduce.js",
                new MapReduceOptions().outputCollection("stock_dy_statistics"), BasicDBObject.class);
        Iterator<BasicDBObject> iter = result.iterator();
        while (iter.hasNext()) {
            BasicDBObject item = iter.next();
            String code = item.getString("_id");
            BasicDBObject value = (BasicDBObject) item.get("value");
            if (value.containsKey("size") && value.getInt("size") > 4) {
                double percent = Double.parseDouble(value.getString("percent"));
                WriteResult wr = template.upsert(
                        new Query(Criteria.where("_id").is(code)),
                        new Update()
                                .set("_id", code)
                                .set("fiveYearDy", percent),
                        "stock"
                );
            }

        }
        return String.format("success:%s - %s", startDate, endDate);
    }


    @ResponseBody
    @RequestMapping(value = "/stock/mapReduce/5years_roe")
    public String fiveYearsRoe() {
        int endYear = StockDateUtil.getCurrentYear();
        int startYear = endYear - 5;
        Query query = new Query();
        query.addCriteria(Criteria.where("year").gte(startYear).lte(endYear));
        MapReduceResults<BasicDBObject> result = template.mapReduce(query, "stock_his_roe",
                "classpath:/mapreduce/5years_roe/map.js", "classpath:/mapreduce/5years_roe/reduce.js",
                new MapReduceOptions().outputCollection("stock_hisroe_statistics"), BasicDBObject.class);
        Iterator<BasicDBObject> iter = result.iterator();
        while (iter.hasNext()) {
            BasicDBObject item = iter.next();
            String code = item.getString("_id");
            BasicDBObject value = (BasicDBObject) item.get("value");
            if (value.containsKey("size") && value.getInt("size") > 4) {
                double percent = Double.parseDouble(value.getString("percent"));
                WriteResult wr = template.upsert(
                        new Query(Criteria.where("_id").is(code)),
                        new Update()
                                .set("_id", code)
                                .set("fiveYearRoe", percent),
                        "stock"
                );
            }

        }
        return String.format("success:%s - %s", startYear, endYear);
    }

}
