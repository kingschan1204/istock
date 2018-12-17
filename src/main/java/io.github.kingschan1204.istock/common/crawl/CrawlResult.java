package io.github.kingschan1204.istock.common.crawl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;

/**
 * 爬虫返回类
 * @author chenguoxiang
 * @create 2018-11-13 16:50
 **/
@Getter
@Setter
public class CrawlResult {

    private String message;
    private Boolean statu;
    private JSONObject data;
    private JSONArray datas;
    private Document page;
}
