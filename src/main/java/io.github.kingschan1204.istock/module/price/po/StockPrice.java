package io.github.kingschan1204.istock.module.price.po;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * price po
 *
 * @author chenguoxiang
 * @create 2018-02-01 15:39
 **/
@Document(collection = "stock_price")
public class StockPrice {

    //{"code":"600519","price":757.73,"yesterdayPrice":764.54,"name":"贵州茅台","fluctuate":-0.9,"today_max":767.3,"today_min":752.92}
    @Id
    private String _id;
    private String code;
    private Double price;
    private Double yesterdayPrice;
    private String name;
    private Double fluctuate;
    private Double todayMax;
    private Double todayMin;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
