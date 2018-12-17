package io.github.kingschan1204.istock.common.crawl;

import io.github.kingschan1204.istock.common.exception.CrawlException;

import java.util.Map;

/**
 * 抽象蜘蛛
 * @author chenguoxiang
 * @create 2018-11-13 16:17
 **/
public interface AbstractSpider {

    CrawlResult crawl( String params) throws CrawlException;


}
