package io.github.kingschan1204.istock.repository;

import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import io.github.kingschan1204.istock.model.vo.StockMasterVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kingschan on 2017/6/28.
 */
@Repository
public interface StockMasterRepository extends PagingAndSortingRepository<StockMasterEntity, String> {

    Page<StockMasterEntity> findAll(Specification<StockMasterEntity> sCode, Pageable pageable);
}
