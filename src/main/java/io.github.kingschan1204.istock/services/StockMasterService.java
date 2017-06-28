package io.github.kingschan1204.istock.services;

import io.github.kingschan1204.istock.common.util.StockUtil;
import io.github.kingschan1204.istock.model.dto.SinaStockPriceDto;
import io.github.kingschan1204.istock.model.dto.StockMasterDto;
import io.github.kingschan1204.istock.model.dto.ThsStockDividendRate;
import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import io.github.kingschan1204.istock.repository.StockMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by kingschan on 2017/6/28.
 */
@Service
public class StockMasterService {
    @Autowired
    private StockMasterRepository stockRepository;

    public void addStock(String code) throws Exception {
        StockMasterEntity stock = stockRepository.findOne(code);
        if (null != stock) {
            throw new Exception("已存在，不要重复添加！");
        }
        List<SinaStockPriceDto> lis = StockUtil.getStockPrice(new String[]{code});
        if (null == lis || lis.size() == 0) {
            throw new Exception("代码不存在！");
        }
        StockMasterDto smd = StockUtil.getStockInfo(code);
        List<ThsStockDividendRate> drs = StockUtil.getStockDividendRate(code);
        stock = new StockMasterEntity();
        stock.setsDividendYear(-1);
        stock.setsDividendRate(BigDecimal.valueOf(-1d));
        if(null!=drs){
            for (int i=0;i<drs.size();i++) {
                ThsStockDividendRate item=drs.get(i);
                if(item.getPercent()!=-1){
                    stock.setsDividendYear(Integer.valueOf(item.getDividendYear().replaceAll("\\D+","")));
                    stock.setsDividendRate(BigDecimal.valueOf(item.getPercent()));
                    break;
                }
            }
        }
        stock.setsCode(lis.get(0).getsCode());
        stock.setsStockName(lis.get(0).getsStockName());
        stock.setsCurrentPrice(lis.get(0).getsCurrentPrice());
        stock.setsYesterdayPrice(lis.get(0).getsYesterdayPrice());
        stock.setsRangePrice(lis.get(0).getsRangePrice());
        stock.setsMainBusiness(smd.getsMainBusiness());
        stock.setsIndustry(smd.getsIndustry());
        stock.setsPeDynamic(smd.getsPeDynamic());
        stock.setsPeStatic(smd.getsPeStatic());
        stock.setsPb(smd.getsPb());
        stock.setsTotalValue(smd.getsTotalValue());
        stock.setsRoe(smd.getsRoe());
        stockRepository.save(stock);
    }
}
