package io.github.kingschan1204.istock.common.db;

import com.mongodb.bulk.BulkWriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author chenguoxiang
 * @create 2019-08-21 16:45
 **/
@Component
public class MyMongoTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 批量保存
     * @param list 数据
     * @param clazz 数据模型
     * @return
     */
    public BulkWriteResult save(List<?> list, Class clazz){
        BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,clazz);
        for (int i = 0; i <list.size() ; i++) {
            ops.insert(list.get(i));
        }
        return ops.execute();
    }

    /**
     * 删除一个集合
     * @param clazz
     */
    public void dropCollection(Class clazz){
        mongoTemplate.dropCollection(clazz);
    }

}
