package io.github.kingschan1204.istock.module.maindata.ctrl;

import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.common.util.stock.impl.DefaultSpiderImpl;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeService;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    @Resource(name = "DefaultSpiderImpl")
    private DefaultSpiderImpl spider;

    @RequestMapping("/stock/q")
    public String queryStock(Integer page, Integer rows, String code, String sidx, String sord) {
        String order = (null == sord || sord.isEmpty()) ? "asc" : sord;
        String field = (null == sidx || sidx.isEmpty()) ? "_id" : sidx;
        return service.queryStock(page, rows, code, field, order);
    }


   @ResponseBody
   @RequestMapping(value = "/stock/init_code",method = RequestMethod.POST)
   public String initCode(){
       try {
           stockCodeService.saveAllStockCode();
           return "success";
       } catch (Exception e) {
           e.printStackTrace();
           return e.getMessage();
       }
   }
}
