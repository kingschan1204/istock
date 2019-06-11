package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 日线价格
 * @author chenguoxiang
 * @create 2019-06-11 16:54
 **/
@Data
@Document(collection = "stock_price_daily")
public class StockPriceDaily {
    @Id
    private String id;
    private String code;
    /**
     * 交易日
     */
    private Integer tradeDate;
    /**
     * 开盘
     */
    private Double open;
    /**
     * 收盘
     */
    private Double close;
    /**
     * 最高
     */
    private Double high;
    /**
     * 最低
     */
    private Double low;
    /**
     * 涨跌额
     */
    private Double change;
    /**
     * 涨跌幅 （未复权
     */
    private Double pctchg;
    /**
     * 成交量 （手）
     */
    private Double vol;
    /**
     * 成交额 （千元）
     */
    private Double amount;

}
