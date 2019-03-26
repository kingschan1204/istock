package io.github.kingschan1204.istock.module.spider.crawl.index;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chenguoxiang
 * @create 2019-03-07 11:18
 **/
@Slf4j
public class IndexCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService ;
    private ScheduledExecutorService scheduledExecutorService2 ;

    private StockCodeInfoService stockCodeInfoService;
    private MongoTemplate template;

    public IndexCrawlJob(StockCodeInfoService stockCodeInfoService,MongoTemplate template){
        scheduledExecutorService = Executors.newScheduledThreadPool(12, new MyThreadFactory("crawlerJob-index"));
        scheduledExecutorService2 = Executors.newScheduledThreadPool(5, new MyThreadFactory("outJob-index"));
        this.stockCodeInfoService=stockCodeInfoService;
        this.template=template;
    }

    public void stopTask(){
        scheduledExecutorService.shutdown();
        scheduledExecutorService2.shutdown();
        Thread.currentThread().interrupt();
    }


    @Override
    public void run() {
        ConcurrentLinkedQueue<Stock> stockQueue = new ConcurrentLinkedQueue<>();
        //sz
        List<StockCodeInfo> sz_codes = stockCodeInfoService.getSZStockCodes();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < sz_codes.size(); i++) {
            list.add(String.format("%s%s", sz_codes.get(i).getType(), sz_codes.get(i).getCode()));
            if (i > 0 && (i % 300 == 0 || i == sz_codes.size() - 1)) {
                try {
                    SinaIndexSpider sinaIndexSpider=  new SinaIndexSpider(list.toArray(new String[]{}), stockQueue);
                    scheduledExecutorService.scheduleAtFixedRate(sinaIndexSpider, 0, 3, TimeUnit.SECONDS);
                    list = new ArrayList<>();
                } catch (Exception ex) {
                    log.error("{}", ex);
                    ex.printStackTrace();
                }
            }

        }

        //sh
        List<StockCodeInfo> sh_codes = stockCodeInfoService.getSHStockCodes();
        list = new ArrayList<>();
        for (int i = 0; i < sh_codes.size(); i++) {
            list.add(String.format("%s%s", sh_codes.get(i).getType(), sh_codes.get(i).getCode()));
            if (i > 0 && (i % 300 == 0 || i == sh_codes.size() - 1)) {
                try {
                    TencentIndexSpider tencentIndexSpider=new TencentIndexSpider(list.toArray(new String[]{}), stockQueue);
                    scheduledExecutorService.scheduleAtFixedRate(tencentIndexSpider, 0, 3, TimeUnit.SECONDS);
                    list = new ArrayList<>();
                    TimeUnit.MILLISECONDS.sleep(800);
                } catch (Exception ex) {
                    log.error("{}", ex);
                    ex.printStackTrace();
                }
            }

        }


        scheduledExecutorService2.execute(new Runnable() {
            @Override
            public void run() {
                while (true && !Thread.currentThread().isInterrupted()) {
                    Stock stock = stockQueue.poll();
                    if (null != stock) {
                        template.upsert(
                                new Query(Criteria.where("_id").is(stock.getCode())),
                                new Update()
                                        .set("_id", stock.getCode())
                                        .set("type", stock.getType())
                                        .set("name", stock.getName())
                                        .set("price", stock.getPrice())
                                        .set("yesterdayPrice", stock.getYesterdayPrice())
                                        .set("fluctuate", stock.getFluctuate())
                                        .set("todayMax", stock.getTodayMax())
                                        .set("todayMin", stock.getTodayMin())
                                        .set("priceDate", stock.getPriceDate()),
                                "stock"
                        );
                    }
                }
            }
        });
    }
}
