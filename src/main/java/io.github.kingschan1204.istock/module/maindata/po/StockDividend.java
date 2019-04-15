package io.github.kingschan1204.istock.module.maindata.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * code
 * title:报告期
 * releaseDate:披露时间
 * plan:分配预案
 * sgbl:送股比例
 * zgbl:转股比例
 * percent:股息率
 * gqdjr:股权登记日
 * cxcqr:除息除权日
 * progress:进度
 *
 * @author chenguoxiang
 * @create 2018-04-12 17:07
 **/
@Document(collection = "stock_dividend")
public class StockDividend {

    @Id
    private String id;
    private String code;
    private String title;
    private String releaseDate;
    private String plan;
    private Integer sgbl;
    private Integer zgbl;
    private Double percent;
    private String gqdjr;
    private String cxcqr;
    private String progress;
    private String from;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Integer getSgbl() {
        return sgbl;
    }

    public void setSgbl(Integer sgbl) {
        this.sgbl = sgbl;
    }

    public Integer getZgbl() {
        return zgbl;
    }

    public void setZgbl(Integer zgbl) {
        this.zgbl = zgbl;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public String getGqdjr() {
        return gqdjr;
    }

    public void setGqdjr(String gqdjr) {
        this.gqdjr = gqdjr;
    }

    public String getCxcqr() {
        return cxcqr;
    }

    public void setCxcqr(String cxcqr) {
        this.cxcqr = cxcqr;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
