package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 历史pb pe
 * @author chenguoxiang
 * @create 2018-07-03 17:56
 **/
@NoArgsConstructor
@Data
@Document(collection = "stock_his_pe_pb")
public class StockHisPbPe {
    /**
     * "_id" : ObjectId("5b3b33799d35576680835a60"),
     "code" : "601088",
     "date" : "2007-10-09",
     "pb" : 19.83924,
     "pe" : 140.56197,
     "price" : 45.3
     * */
    @Id
    private String id;
    private String code;
    private Double pb;
    private Double pe;
    private Double price;
    private String date;
}
