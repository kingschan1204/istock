package io.github.kingschan1204.istock.module.spider;

import io.github.kingschan1204.istock.module.spider.entity.WebPage;

/**
 *
 * @author chenguoxiang
 * @create 2019-03-07 9:26
 **/
public interface ISpider {
    /**
     * 爬取页面
     * @return
     */
    WebPage crawlPage();

    /**
     * 解析页面
     * @param webPage
     */
    void parsePage(WebPage webPage)throws Exception;
}
