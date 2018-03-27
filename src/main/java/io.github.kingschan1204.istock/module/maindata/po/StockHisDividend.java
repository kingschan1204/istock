package io.github.kingschan1204.istock.module.maindata.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ${DESCRIPTION}
 *
 * @author chenguoxiang
 * @create 2018-03-27 18:20
 **/
@Document(collection = "stock_his_dividend")
public class StockHisDividend {

    //{"date":"2017-07-07","code":"600519","year":"2016年报","executeDate":"2017-07-01","remark":"10派67.87元(含税)","percent":1.44}
    @Id
    private String _id;
    private String code;
    private String title;
    private String executeDate;
    private String remark;
    private Double percent;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }


}
