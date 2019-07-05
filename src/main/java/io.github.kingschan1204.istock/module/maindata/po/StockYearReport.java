package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * stock history year report
 * @author chenguoxiang
 * @create 2018-03-09 14:51
 **/
@NoArgsConstructor
@Data
@Document(collection = "stock_year_report")
public class StockYearReport {
    @Id
    private String id;
    private String code;
    private Integer year;
    private Double roe;
    private Double roetb;
    //净利润 亿元
    private Double profits;
    //净利润同比增长率
    private Double profits_percent;
    //营业总收入(亿元)
    private Double operating_income;
    //营业总收入同比增长率
    private Double income_percent;
    //每股净资产
    private Double net_assets;
    //资产负债比率
    private Double asset_liability;
    private Date date;
}
