package io.github.kingschan1204.istock.module.maindata.po;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 上市公司信息
 * @author chenguoxiang
 * @create 2018-10-31 14:35
 **/
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


    public StockCompany(){}

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




    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChairman() {
        return chairman;
    }

    public void setChairman(String chairman) {
        this.chairman = chairman;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public Double getRegCapital() {
        return regCapital;
    }

    public void setRegCapital(Double regCapital) {
        this.regCapital = regCapital;
    }

    public String getSetupDate() {
        return setupDate;
    }

    public void setSetupDate(String setup_date) {
        this.setupDate = setup_date;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Integer getEmployees() {
        return employees;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public String getMainBusiness() {
        return mainBusiness;
    }

    public void setMainBusiness(String main_business) {
        this.mainBusiness = main_business;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }
}
