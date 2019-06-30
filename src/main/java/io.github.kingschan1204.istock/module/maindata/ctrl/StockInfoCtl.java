package io.github.kingschan1204.istock.module.maindata.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.module.maindata.po.StockPriceDaily;
import io.github.kingschan1204.istock.module.maindata.services.StockInfoService;
import io.github.kingschan1204.istock.module.maindata.services.StockPriceServices;
import io.github.kingschan1204.istock.module.maindata.vo.StockVo;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author chenguoxiang
 * @create 2018-11-02 15:49
 **/
@Slf4j
@Controller
public class StockInfoCtl {
    @Autowired
    private StockInfoService stockInfoService;
    @Autowired
    private StockPriceServices stockPriceServices;
    @Autowired
    private TradingDateUtil tradingDateUtil;

    @RequestMapping("/stock/info/{code}")
    public String stockInfo(@PathVariable String code, Model model) {
        JSONObject json= stockInfoService.getStockInfo(code);
        StockVo stockVo = JSON.toJavaObject(json,StockVo.class);
        model.addAttribute("pagetitle",String.format("%s-%s",code,stockVo.getName()));
        model.addAttribute("data",json.toJSONString());
        model.addAttribute("stock", JSON.toJSONString(stockVo));
        return "/stock/info/stock_info";
    }

    @RequestMapping("/stock/info/daily/{code}")
    public String stockDaily(@PathVariable String code, Model model) {
        String startDate= tradingDateUtil.minusDate(1,0,0,"yyyyMMdd");
        String end=tradingDateUtil.getDateYYYYMMdd();
        List<StockPriceDaily> list ;
        if (stockPriceServices.getStockDailyRows(code)==0){
            stockPriceServices.refreshDailyPrice(code,startDate,end);
        }
        list=stockPriceServices.getDailyLine(code);
        StringBuilder data=new StringBuilder();
        for (StockPriceDaily row :list) {
            data.append("[")
                    .append("'").append(row.getTradeDate()).append("'").append(",")
                    .append(row.getOpen()).append(",")
                    .append(row.getClose()).append(",")
                    .append(row.getLow()).append(",")
                    .append(row.getHigh()).append("]")
            .append(",")
            ;
        }
        data.deleteCharAt(data.length()-1);
        model.addAttribute("pagetitle",code);
        model.addAttribute("data",data.toString());
        return "/stock/info/stock_daily";
    }
}
