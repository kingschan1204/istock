package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * code
 * title:报告期
 * releaseDate:披露时间
 * plan:分配预案
 * sgbl:送股比例
 * zgbl:转股比例
 * percent:股息率
 * gqdjr:股权登记日
 * cxcqr:除息除权日
 * progress:进度
 *
 * @author chenguoxiang
 * @create 2018-04-12 17:07
 **/
@NoArgsConstructor
@Data
@Document(collection = "stock_dividend")
public class StockDividend {
    @Id
    private String id;
    private String code;
    private String title;
    private String releaseDate;
    private String plan;
    private Integer sgbl;
    private Integer zgbl;
    private Double percent;
    private String gqdjr;
    private String cxcqr;
    private String progress;
    private String from;
}
