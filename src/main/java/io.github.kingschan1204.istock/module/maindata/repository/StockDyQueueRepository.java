package io.github.kingschan1204.istock.module.maindata.repository;

import io.github.kingschan1204.istock.module.maindata.po.StockDyQueue;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * stock price dao define
 * @author chenguoxiang
 * @create 2018-02-01 15:46
 **/
@Repository
public interface StockDyQueueRepository extends PagingAndSortingRepository<StockDyQueue,String> {

//    Page<Stock> queryAll(String code, Pageable pageable);


}
