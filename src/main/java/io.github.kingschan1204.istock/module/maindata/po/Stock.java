package io.github.kingschan1204.istock.module.maindata.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * stock maindata
 *
 * @author chenguoxiang
 * @create 2018-03-27 10:15
 **/
@Document(collection = "stock")
public class Stock {

    // private String _id;
    @Id
    private String code;
    private String type;
    private String name;
    private Double price;
    private Double yesterdayPrice;
    private Double fluctuate;
    private Double todayMax;
    private Double todayMin;
    private Date priceDate;
    private String industry;
    private String mainBusiness;
    private Double totalValue;
    private Double pb;
    private Double roe;
    private Double bvps;
    private Double pes;
    private Double ped;
    private Date Infodate;

   /* public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }*/

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
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

    public Double getFluctuate() {
        return fluctuate;
    }

    public void setFluctuate(Double fluctuate) {
        this.fluctuate = fluctuate;
    }

    public Double getTodayMax() {
        return todayMax;
    }

    public void setTodayMax(Double todayMax) {
        this.todayMax = todayMax;
    }

    public Double getTodayMin() {
        return todayMin;
    }

    public void setTodayMin(Double todayMin) {
        this.todayMin = todayMin;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
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

    public Date getInfodate() {
        return Infodate;
    }

    public void setInfodate(Date infodate) {
        Infodate = infodate;
    }
}
