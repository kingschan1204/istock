package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * stock maindata
 * @author chenguoxiang
 * @create 2018-03-27 10:15
 **/
@Data
@Document(collection = "stock")
public class Stock {

    @Id
    private String code;
    private String type;
    private String name;
    private Double price;
    private Double yesterdayPrice;
    private Double fluctuate;
    private Double todayMax;
    private Double todayMin;
    private Long priceDate;
    private String industry;
    private Double totalValue;
    private Double pb;
    private Double roe;
    private Double bvps;
    private Double pes;
    private Double ped;
    private Integer infoDate;
    /**
     * 静态分红日期
     */
    private String dividendDate;
    private Double dividend;
    /**
     * 静态分红更新时间
     */
    private Integer dividendUpdateDay;
    /**
     * 实时股息率
     */
    private Double dy;
    /**
     * 实时股息更新时间
     */
    private Integer dyDate;
    /**
     * 5年平均股息
     */
    private Double fiveYearDy;
    /**
     * 5年平均Roe
     */
    private Double fiveYearRoe;

}
