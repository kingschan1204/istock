package io.github.kingschan1204.istock.module.spider.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;

import java.io.Serializable;

/**
 *
 * @author chenguoxiang
 * @create 2019-03-07 9:33
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WebPage implements Serializable {
    private Long jobDoneTime;
    private String pageUrl;
    private Document document;
    private String html;
    private Integer httpCode;
}
