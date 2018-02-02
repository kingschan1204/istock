package io.github.kingschan1204.istock.module.info.repository;

import io.github.kingschan1204.istock.module.info.po.StockInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * stock info dao define
 * @author chenguoxiang
 * @create 2018-02-02 14:39
 **/
@Repository
public interface StockInfoReository extends MongoRepository<StockInfo,String>{

}
