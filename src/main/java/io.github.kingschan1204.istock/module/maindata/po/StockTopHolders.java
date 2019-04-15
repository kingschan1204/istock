package io.github.kingschan1204.istock.module.maindata.po;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * 代码前几名持有人
 * @author chenguoxiang
 * @create 2018-11-01 10:47
 **/
@Data
@Document(collection = "stock_top_holders")
public class StockTopHolders {
    @Id
    private String id;
    private String code;
    private Integer annDate;
    private Integer endDate;
    private String holderName;
    private Double holdAmount;
    private Double holdRatio;

    public StockTopHolders(JSONArray json){
        this.code=json.getString(0).replaceAll("\\D","");
        this.annDate=json.getInteger(1);
        this.endDate=json.getInteger(2);
        this.holderName=json.getString(3);
        this.holdAmount=json.getDouble(4);
        this.holdRatio=json.getDouble(5);
    }
}
