package io.github.kingschan1204.istock.module.maindata.services;

import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.impl.TushareSpider;
import io.github.kingschan1204.istock.module.maindata.po.StockCompany;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chenguoxiang
 * @create 2018-10-31 16:42
 **/
@Slf4j
@Service
public class StockCompanyService {

        @Autowired
        private TushareSpider tushareSpider;
        @Autowired
        private MongoTemplate mongoTemplate;

    /**
     * 代码列表刷新
     */
    public void refreshStockCompany(){
        List<StockCompany> list = new ArrayList<StockCompany>();
        JSONArray rows_sh =tushareSpider.getStockShCompany();
        JSONArray rows_sz =tushareSpider.getStockSZCompany();
        for (int i = 0; i < rows_sh.size(); i++) {
            list.add(new StockCompany(rows_sh.getJSONArray(i)));
        }
        for (int i = 0; i < rows_sz.size(); i++) {
            list.add(new StockCompany(rows_sz.getJSONArray(i)));
        }
        //先删除再新增
        mongoTemplate.dropCollection(StockCompany.class);
        mongoTemplate.insertAll(list);
    }
}
