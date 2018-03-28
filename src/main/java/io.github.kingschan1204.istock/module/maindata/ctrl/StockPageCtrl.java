package io.github.kingschan1204.istock.module.maindata.ctrl;

import io.github.kingschan1204.istock.module.maindata.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author chenguoxiang
 * @create 2018-03-27 15:19
 **/
@Controller
public class StockPageCtrl {

    @Autowired
    private StockService stockService;
        @RequestMapping("/stock/his/{code}")
        public ModelAndView hisdata(@PathVariable String code, Model model){
            ModelAndView mav = new ModelAndView("his");
            //历年分红
            String data =stockService.getStockDividend(code);
            String item[]=data.split("\\|");
            mav.addObject("year",item[0]);
            mav.addObject("percent",item[1]);
            //历年roe
            data=stockService.getStockHisRoe(code);
            String roeItem[]=data.split("\\|");
            mav.addObject("roe_year",roeItem[0]);
            mav.addObject("roe_percent",roeItem[1]);
            return  mav;
        }


}
