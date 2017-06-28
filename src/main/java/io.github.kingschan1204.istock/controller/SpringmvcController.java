package io.github.kingschan1204.istock.controller;

import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import io.github.kingschan1204.istock.repository.StockMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kingschan on 2017/3/9.
 */
@RestController
public class SpringmvcController {

    @Autowired
    private StockMasterRepository stockRepository;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("name","kingschan");
        return "/freemarker/index";
    }

    @RequestMapping("/stock_list")
    public StockMasterEntity stockList(){
        return stockRepository.findOne("000568");
    }
}
