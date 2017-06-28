package io.github.kingschan1204.istock.controller;

import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import io.github.kingschan1204.istock.model.vo.StockMasterPagingVo;
import io.github.kingschan1204.istock.model.vo.StockMasterVo;
import io.github.kingschan1204.istock.repository.StockMasterRepository;
import io.github.kingschan1204.istock.services.StockMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kingschan on 2017/3/9.
 */
@RequestMapping("/stock")
@RestController
public class StockMasterController {

    @Autowired
    private StockMasterService stockServ;

    @RequestMapping("/get/{code}")
    public StockMasterVo getStock(@PathVariable String code) {
        return stockServ.getStock(code);
    }

    /**
     * @param code
     * @return
     */
    @RequestMapping("/add/{code}")
    public String addStock(@PathVariable String code) {
        String msg = "success";
        try {
            stockServ.addStock(code);
        } catch (Exception e) {
            msg = e.getMessage();
            e.printStackTrace();
        }
        return msg;
    }
    @RequestMapping("/list")
    public StockMasterPagingVo stockList(Integer offset,Integer limit,String code,String sort,String order){
        Page<StockMasterVo> p = null;
        StockMasterPagingVo vo = new StockMasterPagingVo();
        try{
            p= stockServ.stockMasterList(
                    null==offset?1:offset/limit+1,
                    limit,
                    code);
            vo.setRows(p.getContent());
            vo.setTotal(p.getTotalElements());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return vo;
    }
}
