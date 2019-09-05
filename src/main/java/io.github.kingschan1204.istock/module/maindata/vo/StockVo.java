package io.github.kingschan1204.istock.module.maindata.vo;

import lombok.Getter;

/**
 * stock 主数据vo
 *
 * @author chenguoxiang
 * @create 2018-10-16 14:41
 * http://localhost/stock/q?rows=20&page=1&sidx=&sord=asc&
 **/
@Getter
public class StockVo {

    private String code;
    private String type;
    private String name;
    private Double price;
    private Double yesterdayPrice;
    private String fluctuate;
    private Double todayMax;
    private Double todayMin;
    private Long priceDate;
    private String industry;
    private String totalValue;
    private String pb;
    private String roe;
    private String bvps;
    private String pes;
    private String ped;
    //pe (TTM)
    private String pettm;

    //52周最高
    private String high52w;
    //52周最低
    private String low52w;

    /**
     * 静态分红日期
     */
    private String dividendDate;
    private String dividend;
    /**
     * 实时股息率
     */
    private String dy;
    /**
     * 5年平均股息
     */
    private String fiveYearDy;
    private String threeYearDy;
    /**
     * 5年平均Roe
     */
    private String fiveYearRoe;
    private String threeYearRoe;
    /**
     * 总营业收入
     */
    private String totalIncome;
    /**
     * 同期对比总营业收入
     */
    private String incomeDiff;
    /**
     * 净利润
     */
    private String totalProfits;
    /**
     * 同期对比净利润
     */
    private String profitsDiff;
    /**
     * 报告期
     */
    private String report;
    private String stype;
    /**
     * 毛利率
     */
    private String mll;

    public void setCode(String code) {
        this.code = code;
    }


    public void setType(String type) {
        this.type = type;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getYesterdayPrice() {
        return yesterdayPrice;
    }

    public void setYesterdayPrice(Double yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
    }


    public void setFluctuate(Double fluctuate) {
        this.fluctuate = String.format("%s%%", fluctuate);
    }


    public void setTodayMax(Double todayMax) {
        this.todayMax = todayMax;
    }


    public void setTodayMin(Double todayMin) {
        this.todayMin = todayMin;
    }


    public void setPriceDate(Long priceDate) {
        this.priceDate = priceDate;
    }


    public void setIndustry(String industry) {
        this.industry = industry;
    }


    public void setTotalValue(Double totalValue) {
        this.totalValue = -1 == totalValue ? "--" : String.format("%s亿", totalValue);
    }


    public void setPb(Double pb) {
        this.pb = -1 == pb ? "--" : String.format("%s", pb);
    }


    public void setRoe(Double roe) {
        this.roe = -1 == roe ? "--" : String.format("%s%%", roe);
    }


    public void setBvps(Double bvps) {
        this.bvps = -1 == bvps ? "--" : String.format("%s", bvps);
    }


    public void setPes(Double pes) {
        this.pes = -1 == pes ? "--" : String.format("%s", pes);
    }


    public void setPed(Double ped) {
        this.ped = -1 == ped ? "--" : String.format("%s", ped);
    }



    public void setDividendDate(String dividendDate) {
        this.dividendDate = dividendDate;
    }


    public void setDividend(Double dividend) {
        if (0 == dividend) {
            this.dividend = "";
            return;
        }
        this.dividend = -1 == dividend ? "--" : String.format("%s%%", dividend);
    }


    public void setDy(Double dy) {
        this.dy = -1 == dy ? "--" : String.format("%s%%", dy);
    }


    public void setFiveYearDy(Double fiveYearDy) {
        this.fiveYearDy = -1 == fiveYearDy ? "--" : String.format("%s%%", fiveYearDy);
    }


    public void setFiveYearRoe(Double fiveYearRoe) {
        this.fiveYearRoe = -1 == fiveYearRoe ? "--" : String.format("%s%%", fiveYearRoe);
    }


    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        if (null != totalIncome) {
            this.totalIncome = totalIncome + "亿";
        }
    }


    public void setIncomeDiff(Double incomeDiff) {
        if (null != incomeDiff) {
            this.incomeDiff = incomeDiff + "%";
        }

    }


    public void setTotalProfits(Double totalProfits) {
        if (null != totalProfits) {
            this.totalProfits = totalProfits + "亿";
        }
    }


    public void setProfitsDiff(Double profitsDiff) {
        if (null != profitsDiff) {
            this.profitsDiff = profitsDiff + "%";
        }
    }


    public void setReport(String report) {
        this.report = report;
    }

    public void setPettm(String pettm) {
        this.pettm = "-1.0".equals(pettm)?"--":pettm;
    }

    public void setHigh52w(String high52w) {
        this.high52w = "-1.0".equals(high52w)?"--":high52w;
    }

    public void setLow52w(String low52w) {
        this.low52w ="-1.0".equals(low52w)?"--":low52w;
    }

    public void setThreeYearDy(Double threeYearDy) {
        this.threeYearDy = threeYearDy==-1?"--":threeYearDy+"%";
    }

    public void setThreeYearRoe(Double threeYearRoe) {
        this.threeYearRoe = threeYearRoe==-1?"--":threeYearRoe+"%";
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public void setMll(Double mll) {
        this.mll = mll+"%";
    }

    public StockVo() {
    }
}
