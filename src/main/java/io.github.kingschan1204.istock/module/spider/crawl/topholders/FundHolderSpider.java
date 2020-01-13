package io.github.kingschan1204.istock.module.spider.crawl.topholders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.db.MyMongoTemplate;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.common.util.spring.SpringMailSender;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.maindata.po.StockFundHolder;
import io.github.kingschan1204.istock.module.spider.AbstractHtmlSpider;
import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimeJobFactory;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class FundHolderSpider extends AbstractHtmlSpider<Stock> {

    //当前要处理的代码
    private StockCodeInfo currentCodeInfo;
    private AtomicInteger error;

    public FundHolderSpider(AtomicInteger error) {
        this.useAgent = SpringContextUtil.getProperties("spider.useagent");
        this.error = error;
    }

    public MongoTemplate getMongoTemp() {
        return SpringContextUtil.getBean(MongoTemplate.class);
    }

    /**
     * 得到当前要处理的代码
     *
     * @return
     */
    private void getCodeInfo() {
        //3天更新一遍
        Integer dateNumber = Integer.valueOf(TradingDateUtil.minusDate(0, 0, 3, "yyyyMMdd"));
        Criteria cr = new Criteria();
        Criteria c1 = Criteria.where("fundHolderDate").lt(dateNumber);
        Criteria c2 = Criteria.where("fundHolderDate").exists(false);
        Query query = new Query(cr.orOperator(c1, c2));
        query.limit(1);
        List<StockCodeInfo> list =getMongoTemp().find(query, StockCodeInfo.class);
        if (null != list && list.size() > 0) {
            currentCodeInfo = list.get(0);
            return;
        }
        currentCodeInfo = null;

    }

    @Override
    public WebPage crawlPage() {
        getCodeInfo();
        if (null == currentCodeInfo) {
            return null;
        }
        if (!currentCodeInfo.getCode().matches("\\d+")) {
            log.error("{} 代码错误，不是有效的代码！", currentCodeInfo.getCode());
            return null;
        }
        String fcode = StockSpider.formatStockCode(currentCodeInfo.getCode());
        this.pageUrl = String.format("https://stock.xueqiu.com/v5/stock/quote.json?symbol=%s&extend=detail", fcode);
        this.cookie = new HashMap<>();
        this.cookie.put("xq_a_token", SpringContextUtil.getProperties("xueqiu.token"));
        this.ignoreContentType = true;
        return super.crawlPage();
    }

    @Override
    public void parsePage(WebPage webPage) throws Exception {
        if (null == webPage) {
            return;
        }
        if (webPage.getHttpCode() == 400) {
            log.error("雪球token失效!");
            if (error.addAndGet(1) > 10) {
                log.error("错误超过10次，即将关闭FUND_HOLDERS任务");
                ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.FUND_HOLDERS).execute(ITimerJob.COMMAND.STOP);
                //发邮件通知
                SpringMailSender mailSender = SpringContextUtil.getBean(SpringMailSender.class);
                String adminMail = SpringContextUtil.getProperties("app.admin.email");
                mailSender.sendSimpleTextMail(adminMail, "istock爬虫通知", "雪球token失效,错误已超10次，线程已关闭，请更新token再重新启动程序！");
            }
            return;
        }
        Document doc = webPage.getDocument();
        JSONObject json = JSON.parseObject(doc.text());
        String report = json.getJSONObject("data").getString("chg_date");
        JSONArray founds = json.getJSONObject("data").getJSONArray("fund_items");
        List<StockFundHolder> rows = new ArrayList<>();
        for (int i = 1; i < founds.size(); i++) {
            rows.add(new StockFundHolder(
                    currentCodeInfo.getCode(), report,
                    founds.getJSONObject(i).getString("org_name_or_fund_name"),
                    founds.getJSONObject(i).getDouble("to_float_shares_ratio"),
                    founds.getJSONObject(i).getDouble("held_num")
            ));
        }
        MyMongoTemplate myMongoTemplate = SpringContextUtil.getBean(MyMongoTemplate.class);
        long dels = myMongoTemplate.remove(StockFundHolder.class, Criteria.where("code").is(currentCodeInfo.getCode()));
        log.info("delete StockFundHolder {}", dels);
        int addnum = myMongoTemplate.save(rows, StockFundHolder.class).getInsertedCount();
        log.info("add StockFundHolder {}", addnum);


    }

}
