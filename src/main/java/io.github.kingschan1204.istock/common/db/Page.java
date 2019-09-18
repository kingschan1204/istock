package io.github.kingschan1204.istock.common.db;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * mongo 分页对象
 * @author chenguoxiang
 * @create 2019-09-18 8:15
 **/
@AllArgsConstructor
@Data
public class Page<T> {
    private Long total;
    private Integer pageIndex;
    private Integer pageSize;
    private List<T> data;

}
