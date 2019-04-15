package io.github.kingschan1204.istock.module.maindata.po;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * 代码前几名持有人
 * @author chenguoxiang
 * @create 2018-11-01 10:47
 **/
@Document(collection = "stock_top_holders")
public class StockTopHolders {
    @Id
    private String id;
    private String code;
    private Integer annDate;
    private Integer endDate;
    private String holderName;
    private Double holdAmount;
    private Double holdRatio;


    public StockTopHolders(){}
    public StockTopHolders(JSONArray json){
        this.code=json.getString(0).replaceAll("\\D","");
        this.annDate=json.getInteger(1);
        this.endDate=json.getInteger(2);
        this.holderName=json.getString(3);
        this.holdAmount=json.getDouble(4);
        this.holdRatio=json.getDouble(5);
    }


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

    public Integer getAnnDate() {
        return annDate;
    }

    public void setAnnDate(Integer annDate) {
        this.annDate = annDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Double getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(Double holdAmount) {
        this.holdAmount = holdAmount;
    }

    public Double getHoldRatio() {
        return holdRatio;
    }

    public void setHoldRatio(Double holdRatio) {
        this.holdRatio = holdRatio;
    }
}
