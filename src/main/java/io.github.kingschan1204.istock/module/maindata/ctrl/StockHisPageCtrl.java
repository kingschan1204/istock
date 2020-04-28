package io.github.kingschan1204.istock.module.maindata.ctrl;

import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.module.maindata.po.*;
import io.github.kingschan1204.istock.module.maindata.repository.StockRepository;
import io.github.kingschan1204.istock.module.maindata.services.StockYearReportService;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Optional;

/**
 * 代码历史数据控制器
 *
 * @author chenguoxiang
 * @create 2018-03-27 15:19
 **/
@Controller
public class StockHisPageCtrl {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockYearReportService stockYearReportService;
    private final String template_path = "/stock/his/";

    @RequestMapping("/stock/his_dy/{code}")
    public ModelAndView hisdata(@PathVariable String code, Model model) {
        ModelAndView mav = new ModelAndView(template_path + "his_dy");
        mav.addObject("year", "''");
        mav.addObject("percent", "0");
        Optional<Stock> stock = stockRepository.findById(code);
        if (!stock.isPresent()) {
            mav.addObject("msg", String.format("代码:%s %s", code, "不存在，或者非A股代码!"));
            return mav;
        }
        mav.addObject("stock", stock.get());
        //历年分红
        List<StockDividend> list = stockService.getStockDividend(code);
        if (null != list && list.size() > 0) {
            StringBuffer year = new StringBuffer();
            StringBuffer percent = new StringBuffer();
            list.stream().forEach(item -> {
                if (item.getPercent() > 0) {
                    percent.append(item.getPercent()).append(",");
                    year.append("'").append(item.getTitle()).append("',");
                }

            });
            String data = String.format("%s|%s", year.toString().replaceAll("\\,$", ""),
                    percent.toString().replaceAll("\\,$", "")
            );
            String item[] = data.split("\\|");
            if (item.length == 2) {
                mav.addObject("year", item[0]);
                mav.addObject("percent", item[1]);
                mav.addObject("rows", list);
            }
        } else {
            mav.addObject("msg", "该股票没有分红信息!");
        }
        return mav;
    }

    @RequestMapping("/stock/financial/{code}")
    public ModelAndView getStockHisRoe(@PathVariable String code) {
        ModelAndView mav = new ModelAndView(template_path + "his_roe");
        mav.addObject("roe_year", "''");
        mav.addObject("roe_percent", "0");
        Optional<Stock> stock = stockRepository.findById(code);
        if (!stock.isPresent()) {
            mav.addObject("msg", String.format("代码:%s %s", code, "不存在，或者非A股代码!"));
            return mav;
        }
        mav.addObject("stock", stock.get());
        List<StockYearReport> list = stockService.getStockHisRoe(code);
        if (null == list || list.size() == 0) {
            try {
                list = stockYearReportService.addStockHisRoe(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mav.addObject("rows", list);
        StringBuffer year = new StringBuffer();
        StringBuffer percent = new StringBuffer();
        list.stream().forEach(item -> {
            if (null!=item.getRoe()&&item.getRoe() > 0) {
                percent.append(item.getRoe()).append(",");
                year.append("'").append(item.getYear()).append("',");
            }

        });
        String data = String.format("%s|%s", year.toString().replaceAll("\\,$", ""),
                percent.toString().replaceAll("\\,$", "")
        );
        String roeItem[] = data.split("\\|");
        mav.addObject("roe_year", roeItem[0]);
        mav.addObject("roe_percent", roeItem[1]);
        return mav;
    }



    @Autowired
    private MongoTemplate template;

    @RequestMapping("/stock/hisdata/{code}")
    public ModelAndView getStockInfo(@PathVariable String code) throws Exception {
        ModelAndView mav = new ModelAndView(template_path + "his_pbpe");
        StringBuilder date = new StringBuilder();
        StringBuilder pb = new StringBuilder();
        StringBuilder pe = new StringBuilder();
        StringBuilder price = new StringBuilder();


        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC,"tradeDate")));
        List<StockDailyBasic> lis = template.find(query, StockDailyBasic.class);
        /*if (null == lis || lis.size() == 0) {
            List<String> data = stockService.crawAndSaveHisPbPe(code);
            //价格》日期》pb》pe
            price.append(data.get(0));
            date.append(data.get(1));
            pb.append(data.get(2));
            pe.append(data.get(3));

        } else {*/
            for (int i = 0; i < lis.size(); i++) {
                StockDailyBasic item = lis.get(i);
                date.append("'").append(item.getTradeDate()).append("'");
                pb.append(item.getPb());
                pe.append(item.getPe());
                price.append(item.getClose());
                if (i != lis.size() - 1) {
                    date.append(",");
                    pb.append(",");
                    pe.append(",");
                    price.append(",");
                }

//            }
        }
        mav.addObject("pb", pb.toString());
        mav.addObject("pe", pe.toString());
        mav.addObject("dates", date.toString());
        mav.addObject("price", price.toString());
        mav.addObject("code", code);
        return mav;
    }

    @RequestMapping("/stock/report/{code}")
    public ModelAndView getStockReport(@PathVariable String code) throws Exception {
        ModelAndView mav = new ModelAndView(template_path + "report");
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        List<StockReport> lis = template.find(query, StockReport.class);
        if (null==lis||lis.size()==0){
            List<String> data = stockService.crawAndSaveHisPbPe(code);
            JSONArray jsons =JSONArray.parseArray(data.get(4));
            lis=jsons.toJavaList(StockReport.class);
        }

        mav.addObject("reports", lis);
        return mav;

    }

}
