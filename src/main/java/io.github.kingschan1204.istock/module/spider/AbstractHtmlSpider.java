package io.github.kingschan1204.istock.module.spider;

import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import io.github.kingschan1204.istock.module.spider.util.JsoupUitl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 抽象爬虫
 *
 * @author chenguoxiang
 * @create 2019-03-07 9:25
 **/
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public abstract class AbstractHtmlSpider<T> implements Runnable, IHtmlSpider {
    //任务执行容器
    protected IJobExecuteContainer jobExecuteContainer;
    protected ConcurrentLinkedQueue<T> queue;
    protected Map<String, String> cookie;
    protected String referer;
    protected String pageUrl;
    protected Integer timeOut;
    protected String useAgent;
    //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    protected Proxy proxy;
    protected Boolean ignoreContentType;
    protected Boolean ignoreHttpErrors;
    protected Connection.Method method;


    @Override
    public WebPage crawlPage() {
        return JsoupUitl.getWebPage(
                pageUrl, method,
                timeOut, useAgent, referer,
                cookie, proxy,
                ignoreContentType, ignoreHttpErrors);
    }

    @Override
    public void run() {
        try {
            WebPage webPage = crawlPage();
            parsePage(webPage);
        } catch (Exception ex) {
            log.error("page process error {} {}", pageUrl, ex);
        }
    }

}
