package io.github.kingschan1204.istock.module.maindata.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import io.github.kingschan1204.istock.common.util.stock.impl.JisiluSpilder;
import io.github.kingschan1204.istock.module.maindata.po.*;
import io.github.kingschan1204.istock.module.maindata.repository.StockRepository;
import io.github.kingschan1204.istock.module.maindata.services.StockHisRoeService;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

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
    private StockHisRoeService stockHisRoeService;
    private final String template_path = "/stock/his/";

    @RequestMapping("/stock/his_dy/{code}")
    public ModelAndView hisdata(@PathVariable String code, Model model) {
        ModelAndView mav = new ModelAndView(template_path + "his_dy");
        mav.addObject("year", "''");
        mav.addObject("percent", "0");
        Stock stock = stockRepository.findOne(code);
        if (null == stock) {
            mav.addObject("msg", String.format("代码:%s %s", code, "不存在，或者非A股代码!"));
            return mav;
        }
        mav.addObject("stock", stock);
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

    @RequestMapping("/stock/his_roe/{code}")
    public ModelAndView getStockHisRoe(@PathVariable String code) {
        ModelAndView mav = new ModelAndView(template_path + "his_roe");
        mav.addObject("roe_year", "''");
        mav.addObject("roe_percent", "0");
        Stock stock = stockRepository.findOne(code);
        if (null == stock) {
            mav.addObject("msg", String.format("代码:%s %s", code, "不存在，或者非A股代码!"));
            return mav;
        }
        mav.addObject("stock", stock);
        List<StockHisRoe> list = stockService.getStockHisRoe(code);
        if (null == list || list.size() == 0) {
            try {
                list = stockHisRoeService.addStockHisRoe(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mav.addObject("rows", list);
        StringBuffer year = new StringBuffer();
        StringBuffer percent = new StringBuffer();
        list.stream().forEach(item -> {
            if (item.getRoe() > 0) {
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


    @RequestMapping("/stock/his_pe/{code}")
    public ModelAndView getStockHisPe(@PathVariable String code) {
        ModelAndView mav = new ModelAndView(template_path + "his_pe");
        mav.addObject("pe_date", "''");
        mav.addObject("pe_value", "0");
        Stock stock = stockRepository.findOne(code);
        if (null == stock) {
            mav.addObject("msg", String.format("代码:%s %s", code, "不存在，或者非A股代码!"));
            return mav;
        }
        mav.addObject("stock", stock);
        List<StockHisPe> list = stockService.getStockHisPe(code);
        if (null == list || list.size() == 0) {
            try {
                list = stockService.addStockHisPe(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mav.addObject("rows", list);
        StringBuffer year = new StringBuffer();
        StringBuffer pe = new StringBuffer();
        list.stream().forEach(item -> {
            if (item.getPe() > 0) {
                pe.append(item.getPe()).append(",");
                year.append("'").append(item.getDate()).append("',");
            }
        });
        String data = String.format("%s|%s", year.toString().replaceAll("\\,$", ""),
                pe.toString().replaceAll("\\,$", "")
        );
        String roeItem[] = data.split("\\|");
        mav.addObject("pe_date", roeItem[0]);
        mav.addObject("pe_value", roeItem[1]);
        return mav;
    }

    @RequestMapping("/stock/his_pb/{code}")
    public ModelAndView getStockHisPb(@PathVariable String code) {
        ModelAndView mav = new ModelAndView(template_path + "his_pb");
        mav.addObject("pb_date", "''");
        mav.addObject("pb_value", "0");
        Stock stock = stockRepository.findOne(code);
        if (null == stock) {
            mav.addObject("msg", String.format("代码:%s %s", code, "不存在，或者非A股代码!"));
            return mav;
        }
        mav.addObject("stock", stock);
        List<StockHisPb> list = stockService.getStockHisPb(code);
        if (null == list || list.size() == 0) {
            try {
                list = stockService.addStockHisPb(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mav.addObject("rows", list);
        StringBuffer year = new StringBuffer();
        StringBuffer pe = new StringBuffer();
        list.stream().forEach(item -> {
            if (item.getPb() > 0) {
                pe.append(item.getPb()).append(",");
                year.append("'").append(item.getDate()).append("',");
            }
        });
        String data = String.format("%s|%s", year.toString().replaceAll("\\,$", ""),
                pe.toString().replaceAll("\\,$", "")
        );
        String roeItem[] = data.split("\\|");
        mav.addObject("pb_date", roeItem[0]);
        mav.addObject("pb_value", roeItem[1]);
        return mav;
    }


    @Autowired
    private MongoTemplate template;

    @RequestMapping("/stock/info/{code}")
    public ModelAndView getStockInfo(@PathVariable String code) throws Exception {
        ModelAndView mav = new ModelAndView(template_path + "his_pbpe");
        StringBuilder date = new StringBuilder();
        StringBuilder pb = new StringBuilder();
        StringBuilder pe = new StringBuilder();
        StringBuilder price = new StringBuilder();


        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        List<StockHisPbPe> lis = template.find(query, StockHisPbPe.class);
        if (null == lis || lis.size() == 0) {
            List<String> data = stockService.crawAndSaveHisPbPe(code);
            //价格》日期》pb》pe
            price.append(data.get(0));
            date.append(data.get(1));
            pb.append(data.get(2));
            pe.append(data.get(3));

        } else {
            for (int i = 0; i < lis.size(); i++) {
                StockHisPbPe item = lis.get(i);
                date.append("'").append(item.getDate()).append("'");
                pb.append(item.getPb());
                pe.append(item.getPe());
                price.append(item.getPrice());
                if (i != lis.size() - 1) {
                    date.append(",");
                    pb.append(",");
                    pe.append(",");
                    price.append(",");
                }

            }
        }
        mav.addObject("pb", pb.toString());
        mav.addObject("pe", pe.toString());
        mav.addObject("dates", date.toString());
        mav.addObject("price", price.toString());
        mav.addObject("code", code);
        return mav;
    }

}
