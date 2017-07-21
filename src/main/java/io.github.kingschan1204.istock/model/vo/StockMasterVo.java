package io.github.kingschan1204.istock.model.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by kingschan on 2017/6/28.
 */
public class StockMasterVo {
    private String sCode;
    private String sStockName;
    private String sCurrentPrice;
    private String sYesterdayPrice;
    private String sRangePrice;
    private String sMainBusiness;
    private String sIndustry;
    private String sPeDynamic;
    private String sPeStatic;
    private String sPb;
    private String sTotalValue;
    private String sRoe;
    private String sDividendRate;
    private String sDividendYear;
    private String sVersion;

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

    public String getsCurrentPrice() {
        return sCurrentPrice;
    }

    public void setsCurrentPrice(String sCurrentPrice) {
        this.sCurrentPrice = sCurrentPrice;
    }

    public String getsYesterdayPrice() {
        return sYesterdayPrice;
    }

    public void setsYesterdayPrice(String sYesterdayPrice) {
        this.sYesterdayPrice = sYesterdayPrice;
    }

    public String getsRangePrice() {
        return sRangePrice;
    }

    public void setsRangePrice(String sRangePrice) {
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

    public String getsPeDynamic() {
        return sPeDynamic;
    }

    public void setsPeDynamic(String sPeDynamic) {
        this.sPeDynamic = sPeDynamic;
    }

    public String getsPeStatic() {
        return sPeStatic;
    }

    public void setsPeStatic(String sPeStatic) {
        this.sPeStatic = sPeStatic;
    }

    public String getsPb() {
        return sPb;
    }

    public void setsPb(String sPb) {
        this.sPb = sPb;
    }

    public String getsTotalValue() {
        return sTotalValue;
    }

    public void setsTotalValue(String sTotalValue) {
        this.sTotalValue = sTotalValue;
    }

    public String getsRoe() {
        return sRoe;
    }

    public void setsRoe(String sRoe) {
        this.sRoe = sRoe;
    }

    public String getsDividendRate() {
        return sDividendRate;
    }

    public void setsDividendRate(String sDividendRate) {
        this.sDividendRate = sDividendRate;
    }

    public String getsDividendYear() {
        return sDividendYear;
    }

    public void setsDividendYear(String sDividendYear) {
        this.sDividendYear = sDividendYear;
    }

    public String getsVersion() {
        return sVersion;
    }

    public void setsVersion(String sVersion) {
        this.sVersion = sVersion;
    }
}
