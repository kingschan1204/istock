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
    private Double profits;
    private Double profits_percent;
    private Double operating_income;
    private Double income_percent;
    private Double net_assets;
    private Double asset_liability;
    private Date date;
}
