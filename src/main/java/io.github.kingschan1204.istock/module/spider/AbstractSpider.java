package io.github.kingschan1204.istock.module.spider;

import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 抽象爬虫
 * @author chenguoxiang
 * @create 2019-03-07 9:25
 **/
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public abstract class AbstractSpider<T> implements Runnable,ISpider{
    protected ConcurrentLinkedQueue<T> queue;
    protected Map<String,String> cookie;
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
        WebPage webPage=null;
        Connection connection =Jsoup.connect(pageUrl)
                .timeout(timeOut)
                .method(method);
        if(null!=ignoreContentType){
            connection.ignoreContentType(ignoreContentType);
        }
        if(null!=ignoreHttpErrors){
            connection.ignoreHttpErrors(ignoreHttpErrors);
        }
       if(null!=referer){
           connection.referrer(referer);
       }
       if(null!=proxy){
           connection.proxy(proxy);
       }
       if(null!=cookie){
           connection.cookies(cookie);
       }
        try {
            Long start=System.currentTimeMillis();
            Connection.Response response =connection.execute();
            Document document=response.parse();
            webPage=new WebPage(System.currentTimeMillis()-start,pageUrl,document,document.html());
            return webPage;
        } catch (IOException e) {
            log.error("crawlPage {} {}",pageUrl,e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        try{
            WebPage webPage=crawlPage();
            parsePage(webPage);
        }catch (Exception ex){
            log.error("page process error {} {}",pageUrl,ex);
        }
    }
}
