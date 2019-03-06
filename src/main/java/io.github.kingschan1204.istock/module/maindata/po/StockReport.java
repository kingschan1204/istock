package io.github.kingschan1204.istock.module.maindata.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 代码定时更新任务
 * @author kings.chan
 * @date 2018-7-6
 */
@Data
@NoArgsConstructor
@Document(collection = "stock_report")
public class StockReport  {
    @Id
    private String id;
    private String code;
    private String releaseDay;
    private String link;
    private String title;
}
