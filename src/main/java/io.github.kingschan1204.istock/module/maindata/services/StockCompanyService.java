package io.github.kingschan1204.istock.module.maindata.services;

import com.alibaba.fastjson.JSONArray;
import io.github.kingschan1204.istock.common.util.stock.impl.TushareSpider;
import io.github.kingschan1204.istock.module.maindata.po.StockCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service
public class StockCompanyService {

        Logger log = LoggerFactory.getLogger(StockCompanyService.class);
        @Autowired
        private TushareSpider tushareSpider;
        @Autowired
        private MongoTemplate mongoTemplate;

    /**
     * 代码列表刷新
     */
    public void refreshStockCompany(){
        List<StockCompany> list = new ArrayList<StockCompany>();
        JSONArray rows =tushareSpider.getStockCompany();
        for (int i = 0; i < rows.size(); i++) {
            list.add(new StockCompany(rows.getJSONArray(i)));
        }
        //先删除再新增
        mongoTemplate.dropCollection(StockCompany.class);
        mongoTemplate.insertAll(list);
    }
}
