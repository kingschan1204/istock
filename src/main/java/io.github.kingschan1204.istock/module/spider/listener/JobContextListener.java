package io.github.kingschan1204.istock.module.spider.listener;
import io.github.kingschan1204.istock.module.spider.crawl.index.IndexCrawlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Slf4j
@WebListener
public class JobContextListener implements ServletContextListener {
//    @Autowired
//    private IndexCrawlJob indexCrawlJob;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.debug("JobContextListener contextInitialized");
//        new Thread(indexCrawlJob).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.debug("JobContextListener contextDestroyed");
    }
}