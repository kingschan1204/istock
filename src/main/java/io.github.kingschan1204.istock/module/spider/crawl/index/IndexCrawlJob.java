package io.github.kingschan1204.istock.module.spider.crawl.index;

import io.github.kingschan1204.istock.common.thread.MyThreadFactory;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author chenguoxiang
 * @create 2019-03-07 11:18
 **/
@Slf4j
public class IndexCrawlJob implements Runnable {

    private ScheduledExecutorService scheduledExecutorService ;
    private ScheduledExecutorService scheduledExecutorService2 ;
    ConcurrentLinkedQueue<Stock> stockQueue = new ConcurrentLinkedQueue<>();

    //scheduleAtFixedRate 也就是规定频率为1h，那么好，A任务开始执行，过来一个小时后，不管A是否执行完，都开启B任务
    //scheduleWithFixedDealy却是需要在A任务执行完后，在经过1小时后再去执行B任务；
    public IndexCrawlJob(){
        scheduledExecutorService = Executors.newScheduledThreadPool(12, new MyThreadFactory("crawlerJob-index"));
        scheduledExecutorService2 = Executors.newScheduledThreadPool(5, new MyThreadFactory("outJob-index"));
    }

    public void stopTask(){
        scheduledExecutorService.shutdown();
        scheduledExecutorService2.shutdown();
        Thread.currentThread().interrupt();
    }

    private void top(){
        ThreadPoolExecutor threadPoolExecutor =(ThreadPoolExecutor)scheduledExecutorService;
        log.info("总线程数{},活动线程数{},执行完成线程数{},排队线程数{},数据队列数{}",
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getActiveCount(),
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getQueue().size(),
                stockQueue.size()
                );
    }

    @Override
    public void run() {
        StockCodeInfoService stockCodeInfoService= SpringContextUtil.getBean(StockCodeInfoService.class);
        MongoTemplate template=SpringContextUtil.getBean(MongoTemplate.class);

        //sz
        List<StockCodeInfo> sz_codes = stockCodeInfoService.getSZStockCodes();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < sz_codes.size(); i++) {
            list.add(String.format("%s%s", sz_codes.get(i).getType(), sz_codes.get(i).getCode()));
            if (i > 0 && (i % 300 == 0 || i == sz_codes.size() - 1)) {
                try {
                    SinaIndexSpider sinaIndexSpider=  new SinaIndexSpider(list.toArray(new String[]{}), stockQueue);
                    scheduledExecutorService.scheduleWithFixedDelay(sinaIndexSpider, 0, 20, TimeUnit.SECONDS);
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
                    scheduledExecutorService.scheduleWithFixedDelay(tencentIndexSpider, 0, 20, TimeUnit.SECONDS);
                    list = new ArrayList<>();
                    TimeUnit.MILLISECONDS.sleep(800);
                } catch (Exception ex) {
                    log.error("{}", ex);
                    ex.printStackTrace();
                }
            }

        }


        scheduledExecutorService2.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                top();
                int index=stockQueue.size()>1000?1000:stockQueue.size();
                if(index==0){return;}
                //这里的BulkMode.UNORDERED是个枚举，，，collectionName是mongo的集合名
                BulkOperations ops = template.bulkOps(BulkOperations.BulkMode.UNORDERED, "stock");
                for (int i = 0; i < index; i++) {
                    Stock stock = stockQueue.poll();
                    ops.updateOne(new Query(Criteria.where("_id").is(stock.getCode())), new Update()
                            .set("_id", stock.getCode())
                            .set("type", stock.getType())
                            .set("name", stock.getName())
                            .set("price", stock.getPrice())
                            .set("yesterdayPrice", stock.getYesterdayPrice())
                            .set("fluctuate", stock.getFluctuate())
                            .set("todayMax", stock.getTodayMax())
                            .set("todayMin", stock.getTodayMin())
                            .set("priceDate", stock.getPriceDate()));
                }
                //循环插完以后批量执行提交一下ok！
                ops.execute();

            }
        }, 0, 2, TimeUnit.SECONDS);
       /* scheduledExecutorService2.execute(new Runnable() {
            @Override
            public void run() {
                while (true && !Thread.currentThread().isInterrupted()) {
                    Stock stock = stockQueue.poll();
                    top();
                    if (null != stock) {
                        log.info("stockQueue size {} : ",stockQueue.size(),scheduledExecutorService );
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
        });*/
    }
}
