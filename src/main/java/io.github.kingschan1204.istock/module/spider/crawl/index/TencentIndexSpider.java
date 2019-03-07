package io.github.kingschan1204.istock.module.spider.crawl.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.util.stock.StockDateUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.spider.AbstractSpider;
import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * http://qt.gtimg.cn/q=%s
 *
 * @author chenguoxiang
 * @create 2019-03-07 10:58
 **/
@Slf4j
public class TencentIndexSpider extends AbstractSpider<Stock> {

    public TencentIndexSpider(String[] stockCode, ConcurrentLinkedQueue<Stock> queue) {
        StringBuilder queryStr = new StringBuilder();
        for (String code : stockCode) {
            String resultCode = StockSpider.formatStockCode(code);
            if (null != resultCode) {
                queryStr.append(resultCode).append(",");
            }
        }
        String queryCode = queryStr.toString().replaceAll("\\,$", "");
        String pageUrl = String.format("http://qt.gtimg.cn/q=%s", queryCode);
        this.queue = queue;
        this.pageUrl = pageUrl;
        this.timeOut = 8000;
        this.ignoreContentType = true;
        this.method = Connection.Method.GET;
    }

    @Override
    public void parsePage(WebPage webPage) throws Exception {

        String content = webPage.getDocument().text();
        List<String> rows = Arrays.asList(content.split(";"));
        JSONArray jsonArray = new JSONArray();
        JSONObject json;
        for (int i = 0; i < rows.size(); i++) {
            String[] item = rows.get(i).replaceAll("v_.*=|\"", "").split("~");
            Stock stock = new Stock();
            stock.setCode(item[2]);
            stock.setFluctuate(Double.parseDouble(item[32]));
            stock.setType(StockSpider.formatStockCode(item[2]).replaceAll("\\d", ""));
            stock.setName(item[1].replaceAll("\\s", ""));
            stock.setPrice(Double.parseDouble(item[3]));
            stock.setTodayMax(Double.parseDouble(item[41]));
            stock.setTodayMin(Double.parseDouble(item[42]));
            stock.setYesterdayPrice(Double.parseDouble(item[4]));
            stock.setPriceDate(StockDateUtil.getCurrentDateTimeNumber());
            queue.offer(stock);

        }
    }
}
