package io.github.kingschan1204.istock.module.hisroe.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * stock history roe
 *
 * @author chenguoxiang
 * @create 2018-03-09 14:51
 **/
@Document(collection = "stock_his_roe")
public class StockHisRoe {


    @Id
    private String _id;
    private String code;
    private Integer year;
    private Double roe;
    private Double roetb;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }

    public Double getRoetb() {
        return roetb;
    }

    public void setRoetb(Double roetb) {
        this.roetb = roetb;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
