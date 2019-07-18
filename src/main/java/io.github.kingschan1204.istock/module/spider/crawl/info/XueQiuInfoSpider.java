package io.github.kingschan1204.istock.module.spider.crawl.info;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import io.github.kingschan1204.istock.module.spider.util.JsoupUitl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * 雪球基本信息爬取
 *
 * @author chenguoxiang
 * @create 2019-03-26 17:57
 * https://stock.xueqiu.com/v5/stock/quote.json?symbol=%s&extend=detail
 **/
@Slf4j
public class XueQiuInfoSpider implements Callable<JSONObject> {
    private final String baseUrl = "https://stock.xueqiu.com/v5/stock/quote.json?symbol=%s&extend=detail";
    private String code;
    private Map<String, String> cookie;
    private String url;

    public XueQiuInfoSpider(String code, String token) {
        this.code = code;
        this.cookie = new HashMap<>();
        this.cookie.put("xq_a_token", token);
        url = String.format(baseUrl,  code);
    }

    @Override
    public JSONObject call() throws Exception {
        WebPage webPage =
        JsoupUitl.getWebPage(
                url, Connection.Method.GET,
                8000, null, "https://xueqiu.com/",
                cookie, null,
                true, true);
        Optional.ofNullable(webPage).map(webPage1 -> webPage1.getDocument().text()).orElseThrow(() -> new Exception("web page null"));
        JSONObject json = JSON.parseObject(webPage.getDocument().text());
        return json;
    }


}
