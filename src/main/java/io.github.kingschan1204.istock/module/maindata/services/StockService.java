package io.github.kingschan1204.istock.module.maindata.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.JisiluSpilder;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockDividend;
import io.github.kingschan1204.istock.module.maindata.po.StockHisRoe;
import io.github.kingschan1204.istock.module.maindata.repository.StockHisDividendRepository;
import io.github.kingschan1204.istock.module.maindata.repository.StockRepository;
import io.github.kingschan1204.istock.module.maindata.vo.StockVo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Stock service
 *
 * @author chenguoxiang
 * @create 2018-03-27 10:27
 **/
@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockHisDividendRepository stockHisDividendRepository;
    @Autowired
    private StockHisRoeService stockHisRoeService;
    @Autowired
    private StockSpider spider;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private JisiluSpilder jisiluSpilder;


    /**
     * 查询股票
     *
     * @param pageindex  页码
     * @param pagesize   显示多少条
     * @param pcode      代码、名称
     * @param type       市场
     * @param pb         市净值
     * @param dy         股息
     * @param orderfidld 排序字段
     * @param psort      排序方式
     * @return
     */
    public String queryStock(int pageindex, int pagesize, final String pcode, final String type, String pb, String dy, String industry, String orderfidld, String psort) {
        Document dbObject = new Document();
        Document fieldObject = new Document();
        fieldObject.put("todayMax", false);
        fieldObject.put("todayMin", false);
        fieldObject.put("priceDate", false);
        fieldObject.put("mainBusiness", false);
        fieldObject.put("dyDate", false);
        fieldObject.put("infoDate", false);
        fieldObject.put("dividendUpdateDay", false);
        Query query = new BasicQuery(dbObject, fieldObject);
        //Projections.exclude(Arrays.asList("todayMax","todayMin","priceDate","mainBusiness","dyDate","infoDate","dividendUpdateDay"));

        Optional<String> code = Optional.ofNullable(pcode);
        if (code.isPresent()) {
            if (pcode.matches("\\d{6}")) {
                query.addCriteria(Criteria.where("_id").is(pcode));
            } else if (pcode.matches("\\d+")) {
                query.addCriteria(Criteria.where("_id").regex(pcode));
            } else {
                query.addCriteria(Criteria.where("name").regex(pcode));
            }
        }
        if (null != type && type.matches("sz|sh")) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (null != pb && pb.matches("\\d+(\\.\\d+)?\\-\\d+(\\.\\d+)?")) {
            double s = Double.parseDouble(pb.split("-")[0]);
            double d = Double.parseDouble(pb.split("-")[1]);
            query.addCriteria(Criteria.where("pb").gte(s).lte(d));
        }
        if (null != dy && dy.matches("(dy|dividend|fiveYearDy)\\-\\d+(\\.\\d+)?\\-\\d+(\\.\\d+)?")) {
            String field = dy.split("-")[0];
            double s = Double.parseDouble(dy.split("-")[1]);
            double d = Double.parseDouble(dy.split("-")[2]);
            query.addCriteria(Criteria.where(field).gte(s).lte(d));
        }
        if (null != industry && !industry.isEmpty()) {
            query.addCriteria(Criteria.where("industry").is(industry));
        }
        //记录总数
        Long total = template.count(query, Stock.class);
        //分页
        query.skip((pageindex - 1) * pagesize).limit(pagesize);
        //排序
        Sort.Direction sortd = "asc".equalsIgnoreCase(psort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(new Sort(sortd, orderfidld));
        //code
        List<Stock> list = template.find(query, Stock.class);
        //原始数据在vo对象里进行格式转换
        List<StockVo> temp = JSON.parseArray(JSON.toJSONString(list), StockVo.class);
        JSONObject data = new JSONObject();
        long pagetotal = total % pagesize == 0 ? total / pagesize : total / pagesize + 1;
        data.put("rows", JSONArray.parseArray(JSON.toJSONString(temp)));
        //有多少页
        data.put("total", pagetotal);
        // 总共有多少条记录
        data.put("records", total);
        data.put("page", pageindex);
        return data.toJSONString();
    }


    /**
     * 得到股票历史分红信息
     *
     * @param code
     * @return
     */
    public List<StockDividend> getStockDividend(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        //排序
        query.with(new Sort(Sort.Direction.ASC, "title"));
        //code
        List<StockDividend> list = template.find(query, StockDividend.class);
        return list;
    }


    /**
     * 得到股票历史roe
     *
     * @param code
     * @return
     */
    public List<StockHisRoe> getStockHisRoe(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        //排序
        query.with(new Sort(Sort.Direction.ASC, "year"));
        //code
        List<StockHisRoe> list = template.find(query, StockHisRoe.class);
        return list;
    }


    /**
     * 抓取历史数据
     *
     * @param code
     * @return
     * @throws Exception
     */
    public List<String> crawAndSaveHisPbPe(String code) throws Exception {

        StringBuilder price = new StringBuilder();
        StringBuilder pe = new StringBuilder();
        StringBuilder pb = new StringBuilder();
        StringBuilder date = new StringBuilder();

        JSONObject data = jisiluSpilder.crawHisPbPePriceAndReports(code);
        List<Document> list = new ArrayList<Document>();
        MongoCollection<Document> hisdata = template.getCollection("stock_his_pe_pb");
        MongoCollection<Document> report = template.getCollection("stock_report");

        JSONArray hisdataJsons = data.getJSONArray("hisdata");
        for (int i = 0; i < hisdataJsons.size(); i++) {
            JSONObject row = hisdataJsons.getJSONObject(i);
            Document object = new Document();
            object.put("code", code);
            object.put("date", row.getString("date"));
            object.put("pb", row.getDouble("pb"));
            object.put("pe", row.getDouble("pe"));
            object.put("price", row.getDouble("price"));
            //顺便拼成字符串
            date.append("'").append(row.getString("date")).append("'");
            price.append(row.getDouble("price"));
            pb.append(row.getDouble("pb"));
            pe.append(row.getDouble("pe"));
            if (i != hisdataJsons.size() - 1) {
                price.append(",");
                pb.append(",");
                pe.append(",");
                date.append(",");
            }

            list.add(object);
            if ((i != 0 && i % 1000 == 0) || i == hisdataJsons.size() - 1) {
                hisdata.insertMany(list);
                list.clear();
            }
        }


        JSONArray reportJsons = data.getJSONArray("reports");
        list = new ArrayList<>();
        for (int i = 0; i < reportJsons.size(); i++) {
            JSONObject row = reportJsons.getJSONObject(i);
            Document object = new Document();
            object.put("code", code);
            object.put("releaseDay", row.getString("releaseDay"));
            object.put("link", row.getString("link"));
            object.put("title", row.getString("title"));
            list.add(object);
            if ((i != 0 && i % 1000 == 0) || i == reportJsons.size() - 1) {
                report.insertMany(list);
                list.clear();
            }
        }
        //价格》日期》pb》pe
        List<String> result = new ArrayList<>();
        result.add(price.toString());
        result.add(date.toString());
        result.add(pb.toString());
        result.add(pe.toString());
        result.add(reportJsons.toJSONString());
        return result;
    }

    /**
     * 更新股票价格
     *
     * @param codes
     * @param spider
     * @throws Exception
     */
  /*  public void updateStockPrice(List<String> codes, StockSpider spider) throws Exception {
        JSONArray jsons = spider.getStockPrice(codes.toArray(new String[]{}));
        List<Stock> stocks = JSON.parseArray(jsons.toJSONString(), Stock.class);
        stocks.stream().forEach(stock -> {
            template.upsert(
                    new Query(Criteria.where("_id").is(stock.getCode())),
                    new Update()
                            .set("_id", stock.getCode())
                            .set("type", stock.getType())
                            .set("name", stock.getName())
                            .set("price", stock.getPrice())
                            .set("yesterdayPrice", stock.getYesterdayPrice())
                            .set("fluctuate", stock.getFluctuate())
                            .set("todayMax", stock.getTodayMax())
                            .set("todayMin", stock.getTodayMin())
                            .set("priceDate", stock.getPriceDate()),
                    "stock"
            );
        });
    }*/

    /**
     * 计算过去5年连续分红的平均股票股息
     *
     * @return
     */
    public void calculateFiveYearsDy() {
        int year = StockDateUtil.getCurrentYear();
        int fiveYearAgo = year - 5;
        String startDate = StockDateUtil.getCurrYearLastDay(fiveYearAgo).toString();
        String endDate = StockDateUtil.getCurrentDate();
        Query query = new Query();
        query.addCriteria(Criteria.where("releaseDate").gte(startDate).lte(endDate));
        MapReduceResults<Document> result = template.mapReduce(query, "stock_dividend",
                "classpath:/mapreduce/5years_dy/dy5years_map.js", "classpath:/mapreduce/5years_dy/dy5years_reduce.js",
                new MapReduceOptions().outputCollection("stock_dy_statistics"), Document.class);
        Iterator<Document> iter = result.iterator();
        while (iter.hasNext()) {
            Document item = iter.next();
            String code = item.getString("_id");
            Document value = (Document) item.get("value");
            if (value.containsKey("size") && value.getDouble("size") > 4) {
                double percent = Double.parseDouble(value.getString("percent"));
                UpdateResult updateResult = template.upsert(
                        new Query(Criteria.where("_id").is(code)),
                        new Update()
                                .set("_id", code)
                                .set("fiveYearDy", percent),
                        "stock"
                );
            }

        }
    }

    /**
     * 计算上市超5年的股息roe
     *
     * @param startYear 开始年份
     * @param endYear   结束年份
     */
    public void calculateFiveYearsRoe(int startYear, int endYear) {
        Query query = new Query();
        query.addCriteria(Criteria.where("year").gte(startYear).lte(endYear));
        MapReduceResults<Document> result = template.mapReduce(query, "stock_his_roe",
                "classpath:/mapreduce/5years_roe/map.js", "classpath:/mapreduce/5years_roe/reduce.js",
                new MapReduceOptions().outputCollection("stock_hisroe_statistics"), Document.class);
        Iterator<Document> iter = result.iterator();
        while (iter.hasNext()) {
            Document item = iter.next();
            String code = item.getString("_id");
            Document value = (Document) item.get("value");
            if (value.containsKey("size") && value.getDouble("size") > 4) {
                double percent = value.getDouble("percent");
                template.upsert(
                        new Query(Criteria.where("_id").is(code)),
                        new Update()
                                .set("_id", code)
                                .set("fiveYearRoe", percent),
                        "stock"
                );
            }

        }
    }

    /**
     * 获取所有行业
     *
     * @return
     */
    public List<String> getAllIntruduce() {
        AggregationResults<Document> a = template.aggregate(
                Aggregation.newAggregation(
                        Aggregation.group("industry").count().as("count"))
                , Stock.class, Document.class);
        List<String> list = new ArrayList<String>();
        if (null != a.getMappedResults()) {
            for (Document doc : a.getMappedResults()) {
                String key = doc.getString("_id");
                if (key.isEmpty()) {
                    continue;
                }
                list.add(key);
            }
        }
        return list;
    }

}
