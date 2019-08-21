package io.github.kingschan1204.istock.module.maindata.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 中证行业分类表
 * @author chenguoxiang
 * @create 2019-08-21 10:16
 **/
@AllArgsConstructor
@Data
@Document(collection = "csindex_industry")
public class CsIndexIndustry {
    @Id
    private String code;
    private String name;
    private String lvone;
    private String lvtwo;
    private String lvthree;
    private String lvfour;
    private String hs300index;
    private Integer date;

}
