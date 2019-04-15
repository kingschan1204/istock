package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * stock history roe
 * @author chenguoxiang
 * @create 2018-03-09 14:51
 **/
@NoArgsConstructor
@Data
@Document(collection = "stock_his_roe")
public class StockHisRoe {
    @Id
    private String id;
    private String code;
    private Integer year;
    private Double roe;
    private Double roetb;
    private Date date;
}
