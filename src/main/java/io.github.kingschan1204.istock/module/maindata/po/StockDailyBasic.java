package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 历史每日基本面
 * @author chenguoxiang
 * @create 2019-06-12 17:49
 **/
@Data
@Document(collection = "stock_daily_basic")
public class StockDailyBasic {
    @Id
    private String id;
    /**
     * 当日收盘价
     */
    private String code;
    private String tradeDate;
    private Double close;
    /**
     * 市盈率（总市值/净利润）
     */
    private Double pe;
    /**
     * 市盈率（TTM）
     */
    private Double peTTM;
    /**
     * 市净率（总市值/净资产）
     */
    private Double pb;
    /**
     * 市销率
     */
    private Double ps;
    /**
     * 市销率（TTM）
     */
    private Double psTTM;
    /**
     * 总股本 （万股）
     */
    private Double totalShare;
    /**
     * 流通股本 （万股）
     */
    private Double floatShare;
    /**
     * 自由流通股本 （万）
     */
    private Double freeShare;
    /**
     * 总市值 （万元）
     */
    private Double totalMv;
    /**
     * 流通市值（万元）
     */
    private Double circMv;
}
