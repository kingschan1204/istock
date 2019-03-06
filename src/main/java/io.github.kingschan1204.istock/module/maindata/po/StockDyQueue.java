package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author chenguoxiang
 * @create 2018-04-02 15:49
 **/
@NoArgsConstructor
@Data
@Document(collection = "stock_dy_queue")
public class StockDyQueue {

    @Id
    private String id;
    private Integer date;
    private Integer pageIndex;
    private Integer totalPage;

}
