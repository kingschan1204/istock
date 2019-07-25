package io.github.kingschan1204.istock.module.maindata.ctrl.api;

import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeInfoService;
import io.github.kingschan1204.istock.module.maindata.services.StockCompanyService;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * @author chenguoxiang
 * @create 2018-03-27 11:14
 **/
@Api(description = "代码管理")
@RequestMapping("/stock")
@Slf4j
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

    @ApiOperation(value = "获取代码列表", notes = "根据传入参数返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "显示条数", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "code", value = "代码、名称", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "市场类别", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pb", value = "市净值", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "dy", value = "股息", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "industry", value = "所属行业", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sidx", value = "排序字段", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sord", value = "排序方式", required = false, paramType = "query", dataType = "string"),
    })
    @GetMapping("/q")
    public String queryStock(Integer page, Integer rows, String code, String type, String pb, String dy,String industry, String sidx, String sord) {
        String order = (null == sord || sord.isEmpty()) ? "asc" : sord;
        String field = (null == sidx || sidx.isEmpty()) ? "_id" : sidx;
        return service.queryStock(page, rows, code, type, pb, dy,industry, field, order);
    }


    @ApiOperation(value = "刷新代码", notes = "初始化所有代码")
    @ResponseBody
    @PostMapping(value = "/refresh_code")
    public String initCode() {
        try {
            stockCodeInfoService.refreshCode();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @ApiOperation(value = "刷新公司", notes = "初始化所有公司信息")
    @ResponseBody
    @PostMapping(value = "/refresh_company")
    public String initCompany() {
        try {
            stockCompanyService.refreshStockCompany();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @ApiOperation(value = "刷新dy值", notes = "刷新5年平均dy值")
    @ResponseBody
    @GetMapping(value = "/mapReduce/dy/{start}/{end}/{key}")
    public String fiveYearsDy(@PathVariable("start") Integer start,@PathVariable("end") Integer end ,@PathVariable("key")String key) {
        service.calculateFiveYearsDy(start,end,key);
        return String.format("success:%s - %s - %s", start, end,key);
    }

    @ApiOperation(value = "刷新roe值", notes = "刷新5年平均roe值")
    @ResponseBody
    @GetMapping(value = "/mapReduce/roe/{start}/{end}/{key}")
    public String fiveYearsRoe(@PathVariable("start") int start,@PathVariable("end") int end ,@PathVariable("key")String key) {
        service.calculateFiveYearsRoe(start,end,key);
        return String.format("success:%s - %s - %s", start, end,key);
    }

}
