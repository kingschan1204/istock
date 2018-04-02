package io.github.kingschan1204.istock.module.maindata.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author chenguoxiang
 * @create 2018-04-02 15:49
 **/
@Document(collection = "stock_dy_queue")
public class StockDyQueue {

    @Id
    private String id;
    private Integer date;
    private Integer pageIndex;
    private Integer totalPage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

}
