package io.github.kingschan1204.istock.controller;

import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import io.github.kingschan1204.istock.repository.StockMasterRepository;
import io.github.kingschan1204.istock.services.StockMasterService;
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
    private StockMasterService stockServ;

    @RequestMapping("/add-stock")
    public String addStock(String code){
        String msg="success";
        try {
            stockServ.addStock(code);
        } catch (Exception e) {
            msg=e.getMessage();
            e.printStackTrace();
        }
        return msg;
    }
}
