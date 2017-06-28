package io.github.kingschan1204.istock.model.po;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by kingschan on 2017/6/28.
 */
@Entity
@Table(name = "stock_master", schema = "stock")
public class StockMasterEntity {
    private String sCode;
    private String sStockName;
    private BigDecimal sCurrentPrice;
    private BigDecimal sYesterdayPrice;
    private BigDecimal sRangePrice;
    private String sMainBusiness;
    private String sIndustry;
    private BigDecimal sPeDynamic;
    private BigDecimal sPeStatic;
    private BigDecimal sPb;
    private BigDecimal sTotalValue;
    private BigDecimal sRoe;
    private BigDecimal sDividendRate;
    private Integer sDividendYear;
    private Timestamp sVersion;

    @Id
    @Column(name = "s_code")
    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    @Basic
    @Column(name = "s_stock_name")
    public String getsStockName() {
        return sStockName;
    }

    public void setsStockName(String sStockName) {
        this.sStockName = sStockName;
    }

    @Basic
    @Column(name = "s_current_price")
    public BigDecimal getsCurrentPrice() {
        return sCurrentPrice;
    }

    public void setsCurrentPrice(BigDecimal sCurrentPrice) {
        this.sCurrentPrice = sCurrentPrice;
    }

    @Basic
    @Column(name = "s_yesterday_price")
    public BigDecimal getsYesterdayPrice() {
        return sYesterdayPrice;
    }

    public void setsYesterdayPrice(BigDecimal sYesterdayPrice) {
        this.sYesterdayPrice = sYesterdayPrice;
    }

    @Basic
    @Column(name = "s_range_price")
    public BigDecimal getsRangePrice() {
        return sRangePrice;
    }

    public void setsRangePrice(BigDecimal sRangePrice) {
        this.sRangePrice = sRangePrice;
    }

    @Basic
    @Column(name = "s_main_business")
    public String getsMainBusiness() {
        return sMainBusiness;
    }

    public void setsMainBusiness(String sMainBusiness) {
        this.sMainBusiness = sMainBusiness;
    }

    @Basic
    @Column(name = "s_industry")
    public String getsIndustry() {
        return sIndustry;
    }

    public void setsIndustry(String sIndustry) {
        this.sIndustry = sIndustry;
    }

    @Basic
    @Column(name = "s_pe_dynamic")
    public BigDecimal getsPeDynamic() {
        return sPeDynamic;
    }

    public void setsPeDynamic(BigDecimal sPeDynamic) {
        this.sPeDynamic = sPeDynamic;
    }

    @Basic
    @Column(name = "s_pe_static")
    public BigDecimal getsPeStatic() {
        return sPeStatic;
    }

    public void setsPeStatic(BigDecimal sPeStatic) {
        this.sPeStatic = sPeStatic;
    }

    @Basic
    @Column(name = "s_pb")
    public BigDecimal getsPb() {
        return sPb;
    }

    public void setsPb(BigDecimal sPb) {
        this.sPb = sPb;
    }

    @Basic
    @Column(name = "s_total_value")
    public BigDecimal getsTotalValue() {
        return sTotalValue;
    }

    public void setsTotalValue(BigDecimal sTotalValue) {
        this.sTotalValue = sTotalValue;
    }

    @Basic
    @Column(name = "s_roe")
    public BigDecimal getsRoe() {
        return sRoe;
    }

    public void setsRoe(BigDecimal sRoe) {
        this.sRoe = sRoe;
    }

    @Basic
    @Column(name = "s_dividend_rate")
    public BigDecimal getsDividendRate() {
        return sDividendRate;
    }

    public void setsDividendRate(BigDecimal sDividendRate) {
        this.sDividendRate = sDividendRate;
    }

    @Basic
    @Column(name = "s_dividend_year")
    public Integer getsDividendYear() {
        return sDividendYear;
    }

    public void setsDividendYear(Integer sDividendYear) {
        this.sDividendYear = sDividendYear;
    }

    @Basic
    @Column(name = "s_version")
    public Timestamp getsVersion() {
        return sVersion;
    }

    public void setsVersion(Timestamp sVersion) {
        this.sVersion = sVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockMasterEntity that = (StockMasterEntity) o;

        if (sCode != null ? !sCode.equals(that.sCode) : that.sCode != null) return false;
        if (sStockName != null ? !sStockName.equals(that.sStockName) : that.sStockName != null) return false;
        if (sCurrentPrice != null ? !sCurrentPrice.equals(that.sCurrentPrice) : that.sCurrentPrice != null)
            return false;
        if (sYesterdayPrice != null ? !sYesterdayPrice.equals(that.sYesterdayPrice) : that.sYesterdayPrice != null)
            return false;
        if (sRangePrice != null ? !sRangePrice.equals(that.sRangePrice) : that.sRangePrice != null) return false;
        if (sMainBusiness != null ? !sMainBusiness.equals(that.sMainBusiness) : that.sMainBusiness != null)
            return false;
        if (sIndustry != null ? !sIndustry.equals(that.sIndustry) : that.sIndustry != null) return false;
        if (sPeDynamic != null ? !sPeDynamic.equals(that.sPeDynamic) : that.sPeDynamic != null) return false;
        if (sPeStatic != null ? !sPeStatic.equals(that.sPeStatic) : that.sPeStatic != null) return false;
        if (sPb != null ? !sPb.equals(that.sPb) : that.sPb != null) return false;
        if (sTotalValue != null ? !sTotalValue.equals(that.sTotalValue) : that.sTotalValue != null) return false;
        if (sRoe != null ? !sRoe.equals(that.sRoe) : that.sRoe != null) return false;
        if (sDividendRate != null ? !sDividendRate.equals(that.sDividendRate) : that.sDividendRate != null)
            return false;
        if (sDividendYear != null ? !sDividendYear.equals(that.sDividendYear) : that.sDividendYear != null)
            return false;
        if (sVersion != null ? !sVersion.equals(that.sVersion) : that.sVersion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sCode != null ? sCode.hashCode() : 0;
        result = 31 * result + (sStockName != null ? sStockName.hashCode() : 0);
        result = 31 * result + (sCurrentPrice != null ? sCurrentPrice.hashCode() : 0);
        result = 31 * result + (sYesterdayPrice != null ? sYesterdayPrice.hashCode() : 0);
        result = 31 * result + (sRangePrice != null ? sRangePrice.hashCode() : 0);
        result = 31 * result + (sMainBusiness != null ? sMainBusiness.hashCode() : 0);
        result = 31 * result + (sIndustry != null ? sIndustry.hashCode() : 0);
        result = 31 * result + (sPeDynamic != null ? sPeDynamic.hashCode() : 0);
        result = 31 * result + (sPeStatic != null ? sPeStatic.hashCode() : 0);
        result = 31 * result + (sPb != null ? sPb.hashCode() : 0);
        result = 31 * result + (sTotalValue != null ? sTotalValue.hashCode() : 0);
        result = 31 * result + (sRoe != null ? sRoe.hashCode() : 0);
        result = 31 * result + (sDividendRate != null ? sDividendRate.hashCode() : 0);
        result = 31 * result + (sDividendYear != null ? sDividendYear.hashCode() : 0);
        result = 31 * result + (sVersion != null ? sVersion.hashCode() : 0);
        return result;
    }
}
