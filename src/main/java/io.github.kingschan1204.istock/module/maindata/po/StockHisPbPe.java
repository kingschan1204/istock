package io.github.kingschan1204.istock.module.maindata.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ${DESCRIPTION}
 *
 * @author chenguoxiang
 * @create 2018-07-03 17:56
 **/
@Document(collection = "stock_his_pe_pb")
public class StockHisPbPe {
    /**
     * "_id" : ObjectId("5b3b33799d35576680835a60"),
     "code" : "601088",
     "date" : "2007-10-09",
     "pb" : 19.83924,
     "pe" : 140.56197,
     "price" : 45.3
     * */

    @Id
    private String id;
    private String code;
    private Double pb;
    private Double pe;
    private Double price;
    private String date;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPb() {
        return pb;
    }

    public void setPb(Double pb) {
        this.pb = pb;
    }

    public Double getPe() {
        return pe;
    }

    public void setPe(Double pe) {
        this.pe = pe;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
