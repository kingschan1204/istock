package io.github.kingschan1204.istock.model.dto;

/**
 * Created by kingschan on 2017/6/28.
 */
public class ThsStockDividendRate {
    /**
     * 年度
     */
    private String dividendYear;
    /**
     * 除权日
     */
    private String sdDate;

    /**
     * 实施日期
     */
    private String executeDate;
    /**
     * 方案
     */
    private String plan;
    /**
     * 分红率
     */
    private Double percent;


    public ThsStockDividendRate(String year, String date, String plan, Double percent,String executeDate) {
        this.dividendYear = year;
        this.sdDate = date;
        this.plan = plan;
        this.percent = percent;
        this.executeDate=executeDate;

    }


    public String getDividendYear() {
        return dividendYear;
    }

    public void setDividendYear(String dividendYear) {
        this.dividendYear = dividendYear;
    }

    public String getSdDate() {
        return sdDate;
    }

    public void setSdDate(String sdDate) {
        this.sdDate = sdDate;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }
}
