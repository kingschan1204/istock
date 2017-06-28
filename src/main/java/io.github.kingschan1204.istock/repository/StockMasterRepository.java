package io.github.kingschan1204.istock.repository;

import io.github.kingschan1204.istock.model.po.StockMasterEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kingschan on 2017/6/28.
 */
@Repository
public interface StockMasterRepository extends PagingAndSortingRepository<StockMasterEntity, String> {

}
