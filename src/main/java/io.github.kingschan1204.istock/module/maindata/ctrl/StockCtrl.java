package io.github.kingschan1204.istock.module.maindata.ctrl;

import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeInfoService;
import io.github.kingschan1204.istock.module.maindata.services.StockCompanyService;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenguoxiang
 * @create 2018-03-27 11:14
 **/
@RestController
public class StockCtrl {

    @Autowired
    private StockService service;
    @Autowired
    private StockCodeInfoService stockCodeInfoService;
    @Autowired
    private StockCompanyService stockCompanyService;
    @Autowired
    private StockSpider spider;

    @RequestMapping("/stock/q")
    public String queryStock(Integer page, Integer rows, String code, String type, String pb, String dy, String sidx, String sord) {
        String order = (null == sord || sord.isEmpty()) ? "asc" : sord;
        String field = (null == sidx || sidx.isEmpty()) ? "_id" : sidx;
        return service.queryStock(page, rows, code, type, pb, dy, field, order);
    }


    @ResponseBody
    @RequestMapping(value = "/stock/refresh_code", method = RequestMethod.POST)
    public String initCode() {
        try {
            stockCodeInfoService.refreshCode();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/stock/refresh_company", method = RequestMethod.POST)
    public String initCompany() {
        try {
            stockCompanyService.refreshStockCompany();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    @ResponseBody
    @RequestMapping(value = "/stock/mapReduce/5years_dy")
    public String fiveYearsDy() {
        service.calculateFiveYearsDy();
        return "success";
    }


    @ResponseBody
    @RequestMapping(value = "/stock/mapReduce/5years_roe")
    public String fiveYearsRoe() {
        int endYear = StockDateUtil.getCurrentYear();
        int startYear = endYear - 5;
        service.calculateFiveYearsRoe(startYear,endYear);
        return String.format("success:%s - %s", startYear, endYear);
    }

}
