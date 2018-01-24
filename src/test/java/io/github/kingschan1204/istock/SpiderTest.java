package io.github.kingschan1204.istock;

import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.stock.StockSpilderUtil;
import io.github.kingschan1204.istock.model.dto.StockMasterDto;
import io.github.kingschan1204.istock.model.dto.ThsStockDividendRate;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author chenguoxiang
 * @create 2018-01-23 19:08
 **/
@SpringBootTest
public class SpiderTest {

    String code="000568";

    @Ignore
    @Test
    public void formatSinaQuryStockCode(){
        System.out.println(StockSpilderUtil.formatSinaQuryStockCode(code));
    }

    @Ignore
    @Test
    public void baseInfo(){
        try {
            StockMasterDto vo =StockSpilderUtil.getStockInfo(code);
            System.out.println(JSONObject.toJSONString(vo));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Ignore
    @Test
    public void getStockDividendRate(){
        //年度财务数据 excel
        //http://basic.10jqka.com.cn/api/stock/export.php?export=main&type=year&code=000568
        try {
            List<ThsStockDividendRate> list =StockSpilderUtil.getStockDividendRate(code);
            list.stream().forEach(t ->{
                System.out.println(JSONObject.toJSONString(t));}
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void demo(){
        try {
            StockSpilderUtil.getHistory(code);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
