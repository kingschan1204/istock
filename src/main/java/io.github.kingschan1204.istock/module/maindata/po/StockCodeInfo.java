package io.github.kingschan1204.istock.module.maindata.po;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 代码信息
 * @author chenguoxiang
 * @create 2018-10-30 15:15
 **/
@Document(collection = "stock_code_info")
public class StockCodeInfo {

    //股票代码
    @Id
    private String code;
    // 深市  沪市
    private String type;
    //股票名称
    private String name;
    //所在地域
    private String area;
    //所属行业
    private String industry;
    //股票全称
    private String fullname;
    //市场类型（主板/中小板/创业板）
    private String market;
    //上市状态L上市 D退市 P暂停上市
    private String list_status;
    //上市日期
    private Integer list_date;


    /**
     * info 信息更新日
     */
    private Integer infoDate;
    /**
     * 持有人更新日
     */
    private Integer holdersDate;

    //init method
    public StockCodeInfo(){    }

    /**
     * data 来源于 tushareSpilder
     * @param data
     */
    public StockCodeInfo(JSONArray data){
        this.code=data.getString(1);
        this.type=data.getString(0).replaceAll("\\d+\\.","").toLowerCase();
        this.name=data.getString(2);
        this.area=data.getString(3);
        this.industry=data.getString(4);
        this.fullname=data.getString(5);
        this.market=data.getString(6);
        this.list_status=data.getString(7);
        this.list_date=data.getInteger(8);
        this.infoDate= 0;
    }




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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getList_status() {
        return list_status;
    }

    public void setList_status(String list_status) {
        this.list_status = list_status;
    }

    public Integer getList_date() {
        return list_date;
    }

    public void setList_date(Integer list_date) {
        this.list_date = list_date;
    }

    public Integer getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(Integer infoDate) {
        this.infoDate = infoDate;
    }

    public Integer getHoldersDate() {
        return holdersDate;
    }

    public void setHoldersDate(Integer holdersDate) {
        this.holdersDate = holdersDate;
    }
}
