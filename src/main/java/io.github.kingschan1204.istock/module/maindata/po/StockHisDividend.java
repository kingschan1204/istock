package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 历史分红
 * @author chenguoxiang
 * @create 2018-03-27 18:20
 **/
@NoArgsConstructor
@Data
@Document(collection = "stock_his_dividend")
public class StockHisDividend {
    @Id
    private String id;
    private String code;
    private String title;
    private String executeDate;
    private String remark;
    private Double percent;

}
