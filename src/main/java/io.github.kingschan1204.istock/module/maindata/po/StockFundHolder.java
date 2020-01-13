package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author chenguoxiang
 * @create 2020-01-13 17:19
 **/
@Document(collection = "stock_fund_holder")
@Data
public class StockFundHolder {
    @Id
    private String id;
    private String code;
    private String report;
    private String fundName;
    private Double percent;
    private Double heldNum;

    public StockFundHolder(String code, String report, String fundName, Double percent, Double heldNum) {
        this.code = code;
        this.report = report;
        this.fundName = fundName;
        this.percent = percent;
        this.heldNum = heldNum;
    }
}
