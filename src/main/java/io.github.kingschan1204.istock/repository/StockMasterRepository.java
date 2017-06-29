package io.github.kingschan1204.istock.repository;

import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import io.github.kingschan1204.istock.model.vo.StockMasterVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Created by kingschan on 2017/6/28.
 */
@Repository
public interface StockMasterRepository extends PagingAndSortingRepository<StockMasterEntity, String> {

    Page<StockMasterEntity> findAll(Specification<StockMasterEntity> sCode, Pageable pageable);

    @Query("select sCode from StockMasterEntity")
    String[] getAllStockCode();

    @Modifying
    @Query("update  StockMasterEntity a set a.sStockName=:name , a.sCurrentPrice=:price ,a.sYesterdayPrice=:yprice ,a.sRangePrice=:rp where a.sCode=:code ")
    void updateIPO(@Param("name")String name, @Param("price")BigDecimal price, @Param("yprice") BigDecimal yprice, @Param("rp")BigDecimal rp, @Param("code")String code);
}
