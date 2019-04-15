package io.github.kingschan1204.istock.module.maindata.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 交易日期
 * @author chenguoxiang
 * @create 2019-03-06 14:44
 **/
@Data
@AllArgsConstructor
@Document(collection = "stock_trade_date")
public class StockTradeDate {
    @Id
    private String date;
    private Boolean isopen;
}
