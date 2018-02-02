package io.github.kingschan1204.istock.module.info.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Stock info po
 *
 * @author chenguoxiang
 * @create 2018-02-02 14:34
 **/
@Document(collection = "stock_info")
public class StockInfo {
   /* {
            "totalValue": 9517.0,
            "date": 1517553336495,
            "pb": 11.28,
            "code": "600519",
            "mainBusiness": "贵州茅台酒系列产品的产品研制、酿造生产、包装和销售。",
            "roe": 24.97,
            "bvps": 67.15,
            "industry": "饮料制造",
            "ped": 35.312,
            "pes": 56.28
    }*/

    @Id
    private String _id;
    private String code;
    private String industry;
    private String mainBusiness;
    private Double totalValue;
    private Double pb;
    private Double roe;
    private Double bvps;
    private Double pes;
    private Double ped;
    private Date date;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getMainBusiness() {
        return mainBusiness;
    }

    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Double getPb() {
        return pb;
    }

    public void setPb(Double pb) {
        this.pb = pb;
    }

    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }

    public Double getBvps() {
        return bvps;
    }

    public void setBvps(Double bvps) {
        this.bvps = bvps;
    }

    public Double getPes() {
        return pes;
    }

    public void setPes(Double pes) {
        this.pes = pes;
    }

    public Double getPed() {
        return ped;
    }

    public void setPed(Double ped) {
        this.ped = ped;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
