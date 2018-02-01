package io.github.kingschan1204.istock.module.price.repository;

import io.github.kingschan1204.istock.module.price.po.StockPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * stock price dao define
 * @author chenguoxiang
 * @create 2018-02-01 15:46
 **/
@Repository
public interface StockPriceRepository extends MongoRepository<StockPrice,String>{

}
