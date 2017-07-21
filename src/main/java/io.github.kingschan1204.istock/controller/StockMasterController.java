package io.github.kingschan1204.istock.controller;

import io.github.kingschan1204.istock.common.util.StockSpilderUtil;
import io.github.kingschan1204.istock.model.dto.ThsStockDividendRate;
import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import io.github.kingschan1204.istock.model.vo.JqGridVo;
import io.github.kingschan1204.istock.model.vo.StockMasterPagingVo;
import io.github.kingschan1204.istock.model.vo.StockMasterVo;
import io.github.kingschan1204.istock.repository.StockMasterRepository;
import io.github.kingschan1204.istock.services.StockMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            stockServ.saveStock(code);
        } catch (Exception e) {
            msg = e.getMessage();
            e.printStackTrace();
        }
        return msg;
    }

    /**
     *
     * @param offset
     * @param limit
     * @param code
     * @param sort 排序的字段
     * @param order 排序的方式
     * @return
     */
    @RequestMapping("/list")
    public StockMasterPagingVo stockList(Integer offset,Integer limit,String code,String sort,String order){
        Page<StockMasterVo> p = null;
        StockMasterPagingVo vo = new StockMasterPagingVo();
        try{
            p= stockServ.stockMasterList(
                    null==offset?1:offset/limit+1,
                    limit,
                    code,
                    sort.equals("true")?null:sort, //默认会传true过来
                    order
            );
            vo.setRows(p.getContent());
            vo.setTotal(p.getTotalElements());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return vo;
    }


    @RequestMapping("/list-jqgrid")
    public JqGridVo stockListJqgrid(Integer page, Integer rows, String code, String sidx, String sord){
        Page<StockMasterVo> p = null;
        JqGridVo vo = new JqGridVo();
        try{
            p= stockServ.stockMasterList(
                    page,
                    rows,
                    code,
                    sidx, //默认会传true过来
                    sord
            );
            vo.setRows(p.getContent());
            vo.setPage(page);
            vo.setRecords(p.getTotalElements());
            vo.setTotal(p.getTotalPages());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return vo;
    }



    @RequestMapping("/refresh")
    public String stockRefresh() {
        String msg = "success";
        try {
            stockServ.stockRefresh();
        } catch (Exception e) {
            msg = e.getMessage();
            e.printStackTrace();
        }
        return msg;
    }

    @RequestMapping("/del")
    public String stockDel(@RequestParam("codes[]") String[] codes){
        String msg = "success";
        try {
            if(codes.length==0){
               return "null";
            }
            stockServ.delStock(codes);
        } catch (Exception e) {
            msg = e.getMessage();
            e.printStackTrace();
        }
        return msg;
    }


    @RequestMapping("/dividendRate/{code}")
    public List<ThsStockDividendRate> dividendRate(@PathVariable String code){
        String msg = "success";
        List<ThsStockDividendRate> lis=null;
        try {
            lis=StockSpilderUtil.getStockDividendRate(code);
        } catch (Exception e) {
            msg = e.getMessage();
            e.printStackTrace();
        }
        return lis;
    }
}
