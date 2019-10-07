package io.github.kingschan1204.istock.module.spider.crawl.info;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.UpdateResult;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.common.util.spring.SpringMailSender;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.spider.AbstractHtmlSpider;
import io.github.kingschan1204.istock.module.spider.dto.XqQuoteDto;
import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimeJobFactory;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class XueQiuQuoteSpider extends AbstractHtmlSpider<Stock> {

    //当前要处理的代码
    private StockCodeInfo currentCodeInfo;
    private AtomicInteger error;

    public XueQiuQuoteSpider(AtomicInteger error) {
        this.useAgent = SpringContextUtil.getProperties("spider.useagent");
        this.error=error;
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
        Integer dateNumber = Integer.valueOf(TradingDateUtil.getDateYYYYMMdd());
        Criteria cr = new Criteria();
        Criteria c1 = Criteria.where("dyDate").lt(dateNumber);
        Criteria c2 = Criteria.where("dyDate").exists(false);
        Query query = new Query(cr.orOperator(c1, c2));
        query.limit(1);
        List<StockCodeInfo> list = getMongoTemp().find(query, StockCodeInfo.class);
        if (null != list && list.size() > 0) {
            this.currentCodeInfo = list.get(0);
        } else {
            try {
                log.info("stock dy 全部更新完毕，关闭更新线程！");
                ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.DY).execute(ITimerJob.COMMAND.STOP);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("{}", e);
            }
        }
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
        if(webPage.getHttpCode()==400){
            log.error("雪球token失效!");
            if(error.addAndGet(1)>10){
                log.error("错误超过10次，即将关闭dy任务");
                ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.DY).execute(ITimerJob.COMMAND.STOP);
                //发邮件通知
                SpringMailSender mailSender = SpringContextUtil.getBean(SpringMailSender.class);
                String adminMail=SpringContextUtil.getProperties("app.admin.email");
                mailSender.sendSimpleTextMail(adminMail,"istock爬虫通知","雪球token失效,错误已超10次，线程已关闭，请更新token再重新启动程序！");
            }
            return;
        }
        Document doc = webPage.getDocument();
        log.info("XueQiu:{}:{}", currentCodeInfo.getCode(), doc.text());
        JSONObject json = JSON.parseObject(doc.text());
        //xueqiu baseinfo
        XqQuoteDto dto = new XqQuoteDto();
        if (json.containsKey("data")) {
            JSONObject detail = json.getJSONObject("data").getJSONObject("quote");
            dto = detail.toJavaObject(XqQuoteDto.class);
        }
        UpdateResult updateResult = getMongoTemp().upsert(
                new Query(Criteria.where("_id").is(currentCodeInfo.getCode())),
                new Update()
                        .set("pettm", dto.getPe_ttm())
                        .set("high52w", dto.getHigh52w())
                        .set("low52w", dto.getLow52w())
                        .set("dy", dto.getDividend_yield())
                ,
                //,
                "stock"
        );
        UpdateResult updateResult2 = getMongoTemp().upsert(
                new Query(Criteria.where("_id").is(currentCodeInfo.getCode())),
                new Update().set("dyDate", Integer.valueOf(TradingDateUtil.getDateYYYYMMdd())), "stock_code_info");
        log.info("XueQiu-dy更新，代码{}受影响行数:{} ", currentCodeInfo.getCode(), updateResult.getModifiedCount() + updateResult2.getModifiedCount());
    }

}
