package io.github.kingschan1204.istock.model.vo;

import java.util.List;

/**
 * Created by kingschan on 2017/6/28.
 */
public class StockMasterPagingVo {
    public List<StockMasterVo> getRows() {
        return rows;
    }

    public void setRows(List<StockMasterVo> rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    private List<StockMasterVo> rows;
    private Long total;
}
