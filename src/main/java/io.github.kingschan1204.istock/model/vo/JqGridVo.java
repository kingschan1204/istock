package io.github.kingschan1204.istock.model.vo;

import java.util.List;

/**
 * Created by kingschan on 2017/7/21.
 */
public class JqGridVo<T> {

    private List<T> rows;// json中代表实际模型数据的入口
    private int total;// json中代表页码总数的数据
    private Long records;// json中代表数据行总数的数据
    private Integer page;// json中代表当前页码的数据

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Long getRecords() {
        return records;
    }

    public void setRecords(Long records) {
        this.records = records;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}
