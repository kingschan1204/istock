package io.github.kingschan1204.istock.module.maindata.repository;

import io.github.kingschan1204.istock.module.maindata.po.StockYearReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author chenguoxiang
 * @create 2018-03-09 14:55
 **/
@Repository
public interface StockYearReportRepository extends MongoRepository<StockYearReport,String> {

}
