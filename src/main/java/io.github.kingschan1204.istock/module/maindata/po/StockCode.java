package io.github.kingschan1204.istock.module.maindata.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * stock code
 * @author chenguoxiang
 * @create 2018-04-08 16:40
 **/
@Document(collection = "stock_code")
public class StockCode {

    @Id
    private String code;

    public StockCode(){}
    public StockCode(String code){this.code=code;}


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
