package io.github.kingschan1204.istock.module.spider.crawl.info;

import com.mongodb.client.result.UpdateResult;
import io.github.kingschan1204.istock.common.util.spring.SpringContextUtil;
import io.github.kingschan1204.istock.common.util.stock.StockSpider;
import io.github.kingschan1204.istock.module.maindata.po.Stock;
import io.github.kingschan1204.istock.module.maindata.po.StockCodeInfo;
import io.github.kingschan1204.istock.module.spider.AbstractHtmlSpider;
import io.github.kingschan1204.istock.module.spider.entity.WebPage;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimeJobFactory;
import io.github.kingschan1204.istock.module.spider.timerjob.ITimerJob;
import io.github.kingschan1204.istock.module.spider.util.MathFormat;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * 同花顺Info信息爬虫
 * @author chenguoxiang
 * @create 2019-03-26 14:10
 **/
@Slf4j
public class ThsInfoSpider extends AbstractHtmlSpider<Stock> {

    //当前要处理的代码
    private StockCodeInfo currentCodeInfo;

    public ThsInfoSpider(String useAgent){
        this.useAgent=useAgent;
    }

    public MongoTemplate getMongoTemp(){
        return SpringContextUtil.getBean(MongoTemplate.class);
    }

    /**
     * 得到当前要处理的代码
     * @return
     */
    private void getCodeInfo(){
        Integer dateNumber = Integer.valueOf(TradingDateUtil.getDateYYYYMMdd());
        Criteria cr = new Criteria();
        Criteria c1 = Criteria.where("infoDate").lt(dateNumber);
        Criteria c2 = Criteria.where("infoDate").exists(false);
        Query query = new Query(cr.orOperator(c1,c2));
        query.limit(1);
        List<StockCodeInfo> list = getMongoTemp().find(query, StockCodeInfo.class);
        if(null!=list&&list.size()>0){
            this.currentCodeInfo=list.get(0);
        }else{
            try {
                log.info("stock info 全部更新完毕，关闭更新线程！");
                ITimeJobFactory.getJob(ITimeJobFactory.TIMEJOB.INFO).execute(ITimerJob.COMMAND.STOP);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("{}",e);
            }
        }
    }

    @Override
    public WebPage crawlPage() {
        getCodeInfo();
        if(null==currentCodeInfo){
            return null;
        }
        this.pageUrl= String.format("http://basic.10jqka.com.cn/%s/", currentCodeInfo.getCode());
        return super.crawlPage();
    }

    @Override
    public void parsePage(WebPage webPage) throws Exception {
        if(null==webPage){
            return;
        }
        String regex = ".*\\：|\\s*";
        Document doc = webPage.getDocument();
        Elements table = doc.getElementsByTag("table");
        //第一个表格的第一行
        Elements tds = table.get(0).select("tr").get(0).select("td");
        //主营业务
//        String zyyw = tds.get(0).text().replaceAll(regex, "");
        //所属行业
        String sshy = tds.get(1).text().replaceAll(regex, "");
        Elements tds1 = table.get(1).select("td");
        //市盈率(动态)
        String dtsyl = tds1.get(0).text().replaceAll(regex, "");
        //市盈率(静态)
        String sjljt = tds1.get(4).text().replaceAll(regex, "");
        //5 营业总收入
        String yyzsr=tds1.get(5).text();
        //市净率
        String sjl = tds1.get(8).text().replaceAll(regex, "");
        //9 净利润
        String jlr=tds1.get(9).text();
        //总市值
        String zsz = tds1.get(11).text().replaceAll("\\D+", "");
        //每股净资产
        double mgjzc = MathFormat.doubleFormat(tds1.get(12).text().replaceAll("\\[.*|", ""));
        String jzcsyl = "-1";
        if (tds1.size() > 14) {
            //净资产收益率
            jzcsyl = tds1.get(14).select("span").get(1).text();
        }
        //提取正负小数正则
        String rege_number="[-+]?([0]{1}(\\.[0-9]+)?|[1-9]{1}\\d*(\\.[0-9]+)?)";
        //关键字替换为- 负数是用中文表示所以得转换
        String down_flag="同比下降";
        //净利润
        jlr=jlr.replaceAll("\\[.*|\\s|\\：","").replace(down_flag,"-").replace("未公布","0,");
        //营业总收入
        yyzsr=yyzsr.replaceAll("\\[.*|\\s|\\：","").replace(down_flag,"-").replace("未公布","0,");
        String jlr_data[]=StockSpider.findStrByRegx(jlr,rege_number).split(",");
        String yyzsr_data[]=StockSpider.findStrByRegx(yyzsr,rege_number).split(",");

        String totalProfits=jlr_data.length>0?jlr_data[0]:"0";
        String profitsDiff=jlr_data.length>1?jlr_data[1]:"0";
        String totalIncome=yyzsr_data.length>0?yyzsr_data[0]:"0";
        String incomeDiff=yyzsr_data.length>1?yyzsr_data[1]:"0";
        //报告期
        Elements element =doc.getElementsByAttributeValue("style","margin-top: 4px;margin-right: 10px;color:#666");
        String report_date=null==element?"":element.text().trim().replace("以上为","");

        UpdateResult updateResult = getMongoTemp().upsert(
                new Query(Criteria.where("_id").is(currentCodeInfo.getCode())),
                new Update()
                        .set("_id", currentCodeInfo.getCode())
                        .set("industry", sshy)
//                        .set("mainBusiness", zyyw)
                        .set("totalValue", MathFormat.doubleFormat(zsz))
                        .set("pb", MathFormat.doubleFormat(sjl))
                        .set("roe", MathFormat.doubleFormat(jzcsyl))
                        .set("bvps", mgjzc)
                        .set("pes", MathFormat.doubleFormat(sjljt))
                        .set("ped", MathFormat.doubleFormat(dtsyl))
                        .set("totalProfits",Double.parseDouble(totalProfits))
                        .set("profitsDiff",Double.parseDouble(profitsDiff))
                        .set("totalIncome",Double.parseDouble(totalIncome))
                        .set("incomeDiff",Double.parseDouble(incomeDiff))
                        .set("report",report_date)
                ,
                //,
                "stock"
        );
        UpdateResult updateResult2 = getMongoTemp().upsert(
                new Query(Criteria.where("_id").is(currentCodeInfo.getCode())),
                new Update().set("infoDate", Integer.valueOf(TradingDateUtil.getDateYYYYMMdd())),"stock_code_info");
        log.info("代码{}受影响行数:{}",currentCodeInfo.getCode(),updateResult.getModifiedCount()+updateResult2.getModifiedCount());
    }

}
