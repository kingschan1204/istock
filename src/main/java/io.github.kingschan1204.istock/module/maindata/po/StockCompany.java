package io.github.kingschan1204.istock.module.maindata.po;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 上市公司信息
 * @author chenguoxiang
 * @create 2018-10-31 14:35
 **/
@NoArgsConstructor
@Data
@Document(collection = "stock_company")
public class StockCompany {

    @Id
    private String code;
    private String chairman;
    private String manager;
    private String secretary;
    private Double regCapital;
    private String setupDate;
    private String province;
    private String city;
    private String introduction;
    private String website;
    private String email;
    private String office;
    private Integer employees;
    private String mainBusiness;
    private String businessScope;

    public StockCompany(JSONArray json){
        this.code=json.getString(0).replaceAll("\\D+","");
        this.chairman=json.getString(1);
        this.manager=json.getString(2);
        this.secretary=json.getString(3);
        this.regCapital=json.getDouble(4);
        this.setupDate=json.getString(5);
        this.province=json.getString(6);
        this.city=json.getString(7);
        this.introduction=json.getString(8);
        this.website=json.getString(9);
        this.email=json.getString(10);
        this.office=json.getString(11);
        this.mainBusiness=json.getString(12);
        this.employees=json.getInteger(13);
        if (null!=json.get(14)){
            this.businessScope=json.getString(14).replaceFirst("^主.*\\:","");
        }

    }
}
