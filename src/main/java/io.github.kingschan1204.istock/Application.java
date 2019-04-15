package io.github.kingschan1204.istock;

import io.github.kingschan1204.istock.common.startup.InitQuartzTaskRunner;
import io.github.kingschan1204.istock.module.maindata.services.StockCodeInfoService;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import io.github.kingschan1204.istock.module.spider.crawl.index.IndexCrawlJob;
import io.github.kingschan1204.istock.module.spider.crawl.info.InfoCrawlJob;
import io.github.kingschan1204.istock.module.spider.schedule.ScheduleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * spring boot 启动类
 *
 * @author kings.chan
 */
@Controller
@EnableCaching
@ServletComponentScan
@SpringBootApplication
public class Application {

    @Autowired
    private StockService stockService;
    @Autowired
    private StockCodeInfoService stockCodeInfoService;
    @Autowired
    private MongoTemplate template;

    private static IndexCrawlJob indexCrawlJob;
    private static InfoCrawlJob infoCrawlJob;
    private static ScheduleJob scheduleJob;

    @ResponseBody
    @RequestMapping("/start")
    public String start()throws Exception{
//        indexCrawlJob=new IndexCrawlJob(stockCodeInfoService,template);
//        Thread thread=new Thread(indexCrawlJob);
//        infoCrawlJob = new InfoCrawlJob(template);
        scheduleJob=new ScheduleJob();
        Thread thread=new Thread(scheduleJob);
        thread.setDaemon(true);
        thread.start();
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/stop")
    public String stop()throws Exception{
        scheduleJob.stopTask();
        return "ok";
    }

    @RequestMapping("/")
    public String index(Model model) {
        List<String> list = stockService.getAllIntruduce();
        model.addAttribute("industry", list);
        return "index";
    }

    //    @Bean
    public InitQuartzTaskRunner startupRunner() {
        return new InitQuartzTaskRunner();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        Thread thread=new Thread(new ScheduleJob());
        thread.setDaemon(true);
        thread.start();
    }
}