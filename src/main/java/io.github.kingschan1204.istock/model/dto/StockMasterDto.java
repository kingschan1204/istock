package io.github.kingschan1204.istock.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by kingschan on 2017/6/28.
 */
public class StockMasterDto {
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

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getsStockName() {
        return sStockName;
    }

    public void setsStockName(String sStockName) {
        this.sStockName = sStockName;
    }

    public BigDecimal getsCurrentPrice() {
        return sCurrentPrice;
    }

    public void setsCurrentPrice(BigDecimal sCurrentPrice) {
        this.sCurrentPrice = sCurrentPrice;
    }

    public BigDecimal getsYesterdayPrice() {
        return sYesterdayPrice;
    }

    public void setsYesterdayPrice(BigDecimal sYesterdayPrice) {
        this.sYesterdayPrice = sYesterdayPrice;
    }

    public BigDecimal getsRangePrice() {
        return sRangePrice;
    }

    public void setsRangePrice(BigDecimal sRangePrice) {
        this.sRangePrice = sRangePrice;
    }

    public String getsMainBusiness() {
        return sMainBusiness;
    }

    public void setsMainBusiness(String sMainBusiness) {
        this.sMainBusiness = sMainBusiness;
    }

    public String getsIndustry() {
        return sIndustry;
    }

    public void setsIndustry(String sIndustry) {
        this.sIndustry = sIndustry;
    }

    public BigDecimal getsPeDynamic() {
        return sPeDynamic;
    }

    public void setsPeDynamic(BigDecimal sPeDynamic) {
        this.sPeDynamic = sPeDynamic;
    }

    public BigDecimal getsPeStatic() {
        return sPeStatic;
    }

    public void setsPeStatic(BigDecimal sPeStatic) {
        this.sPeStatic = sPeStatic;
    }

    public BigDecimal getsPb() {
        return sPb;
    }

    public void setsPb(BigDecimal sPb) {
        this.sPb = sPb;
    }

    public BigDecimal getsTotalValue() {
        return sTotalValue;
    }

    public void setsTotalValue(BigDecimal sTotalValue) {
        this.sTotalValue = sTotalValue;
    }

    public BigDecimal getsRoe() {
        return sRoe;
    }

    public void setsRoe(BigDecimal sRoe) {
        this.sRoe = sRoe;
    }

    public BigDecimal getsDividendRate() {
        return sDividendRate;
    }

    public void setsDividendRate(BigDecimal sDividendRate) {
        this.sDividendRate = sDividendRate;
    }

    public Integer getsDividendYear() {
        return sDividendYear;
    }

    public void setsDividendYear(Integer sDividendYear) {
        this.sDividendYear = sDividendYear;
    }
}
