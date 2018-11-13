package io.github.kingschan1204.istock.module.maindata.services;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.WriteResult;
import io.github.kingschan1204.istock.common.util.stock.impl.TushareSpider;
import io.github.kingschan1204.istock.module.maindata.po.StockTopHolders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码前几名持有人
 * @author chenguoxiang
 * @create 2018-11-01 10:55
 **/
@Service
public class StockTopHoldersService {
    private Logger log = LoggerFactory.getLogger(StockTopHoldersService.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TushareSpider tushareSpider;

    /**
     * 刷新持有人
     * @param code
     */
    public void refreshTopHolders(String code){
        List<StockTopHolders> list = new ArrayList<StockTopHolders>();
        JSONArray rows =tushareSpider.getStockTopHolders(code);
        String frist=null;
        for (int i = 0; i < rows.size(); i++) {
            if(null==frist){
                frist=rows.getJSONArray(i).getString(1);
            }
            if(frist.equals(rows.getJSONArray(i).getString(1))){
                list.add(new StockTopHolders(rows.getJSONArray(i)));
            }else{
                break;
            }
        }
        String query_code=code.replaceAll("\\D+","");
        //先删除再新增
        WriteResult wr= mongoTemplate.remove(new Query(Criteria.where("code").is(query_code)),StockTopHolders.class);
        mongoTemplate.insertAll(list);
        log.info("top10 holders :remove {} ,insert {} ",wr.getN(),list.size());
    }

}
