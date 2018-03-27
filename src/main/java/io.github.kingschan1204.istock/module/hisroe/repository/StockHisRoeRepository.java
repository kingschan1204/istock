package io.github.kingschan1204.istock.module.hisroe.repository;

import io.github.kingschan1204.istock.module.hisroe.po.StockHisRoe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author chenguoxiang
 * @create 2018-03-09 14:55
 **/
@Repository
public interface StockHisRoeRepository extends MongoRepository<StockHisRoe,String> {

}
