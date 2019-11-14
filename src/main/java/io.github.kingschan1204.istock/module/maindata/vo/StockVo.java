package io.github.kingschan1204.istock.module.maindata.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * stock 主数据vo
 *
 * @author chenguoxiang
 * @create 2018null0null6 14:41
 * http://localhost/stock/q?rows=20&page=1&sidx=&sord=asc&
 **/
@Getter
@Setter
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




    public void setFluctuate(Double fluctuate) {
        this.fluctuate = String.format("%s%%", fluctuate);
    }


    public void setTotalValue(Double totalValue) {
        this.totalValue = null == totalValue ? "--" : String.format("%s 亿", totalValue);
    }


    public void setPb(Double pb) {
        this.pb = null == pb ? "--" : String.format("%s", pb);
    }


    public void setRoe(Double roe) {
        this.roe = null == roe ? "--" : String.format("%s%%", roe);
    }


    public void setBvps(Double bvps) {
        this.bvps = null == bvps ? "--" : String.format("%s", bvps);
    }


    public void setPes(Double pes) {
        this.pes = null == pes||-1==pes ? "--" : String.format("%s", pes);
    }


    public void setPed(Double ped) {
        this.ped = null == ped||-1==ped ? "--" : String.format("%s", ped);
    }

    public void setDividend(Double dividend) {
        if (0 == dividend) {
            this.dividend = "";
            return;
        }
        this.dividend = null == dividend ? "--" : String.format("%s%%", dividend);
    }


    public void setDy(Double dy) {
        this.dy = (null == dy||dy<=0) ? "--" : String.format("%s%%", dy);
    }


    public void setFiveYearDy(Double fiveYearDy) {
        this.fiveYearDy = null == fiveYearDy ? "--" : String.format("%s%%", fiveYearDy);
    }


    public void setFiveYearRoe(Double fiveYearRoe) {
        this.fiveYearRoe = null == fiveYearRoe ? "--" : String.format("%s%%", fiveYearRoe);
    }


    public void setTotalIncome(Double totalIncome) {
        if (null != totalIncome) {
            this.totalIncome = totalIncome + " 亿";
        }
    }


    public void setIncomeDiff(Double incomeDiff) {
        if (null != incomeDiff) {
            this.incomeDiff = incomeDiff + "%";
        }

    }


    public void setTotalProfits(Double totalProfits) {
        if (null != totalProfits) {
            this.totalProfits = totalProfits + " 亿";
        }
    }


    public void setProfitsDiff(Double profitsDiff) {
        if (null != profitsDiff) {
            this.profitsDiff = profitsDiff + "%";
        }
    }

    public void setThreeYearDy(Double threeYearDy) {
        this.threeYearDy = threeYearDy==null?"--":threeYearDy+"%";
    }

    public void setThreeYearRoe(Double threeYearRoe) {
        this.threeYearRoe = threeYearRoe==null?"--":threeYearRoe+"%";
    }


    public void setMll(Double mll) {
        this.mll = mll+"%";
    }

    public StockVo() {
    }
}
