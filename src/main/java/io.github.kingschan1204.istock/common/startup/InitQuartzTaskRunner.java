package io.github.kingschan1204.istock.common.startup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.db.MyMongoTemplate;
import io.github.kingschan1204.istock.common.util.cache.EhcacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenguoxiang
 * @create 2018-07-13 15:12
 **/
public class InitQuartzTaskRunner implements ApplicationRunner, Ordered {

    @Autowired
    private MyMongoTemplate mongoTemplate;
    @Autowired
    private EhcacheUtil ehcacheUtil;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        init_ths_type();
        init_zzfl();

    }

    /**
     * 中证4级分类
     *
     * @return
     */
    void init_zzfl() {
        List<String> type = new ArrayList<>();
        JSONObject temp = mongoTemplate.groupBy(null, "csindex_industry", "lvone");
        JSONArray lvones = temp.getJSONArray("retval");
        JSONObject row;
        for (int i = 0; i < lvones.size(); i++) {
            row = lvones.getJSONObject(i);
            String one_key = row.getString("lvone");
            type.add(one_key);
            JSONArray lvtwos = mongoTemplate.groupBy(Criteria.where("lvone").is(one_key), "csindex_industry", "lvtwo").getJSONArray("retval");
            for (int j = 0; j < lvtwos.size(); j++) {
                JSONObject lvtwo_row = lvtwos.getJSONObject(j);
                String two_key = lvtwo_row.getString("lvtwo");
                type.add(String.format("%s %s", "|--", two_key));
                JSONArray lvthrees = mongoTemplate.groupBy(Criteria.where("lvtwo").is(two_key), "csindex_industry", "lvthree").getJSONArray("retval");
                for (int k = 0; k < lvthrees.size(); k++) {
                    JSONObject lvthree_row = lvthrees.getJSONObject(k);
                    String three_key = lvthree_row.getString("lvthree");
                    type.add(String.format("%s %s", "|---", three_key));
                    JSONArray lvfours = mongoTemplate.groupBy(Criteria.where("lvthree").is(three_key), "csindex_industry", "lvfour").getJSONArray("retval");
                    for (int l = 0; l < lvfours.size(); l++) {
                        JSONObject lvfour_row = lvfours.getJSONObject(l);
                        String four_key = lvfour_row.getString("lvfour");
                        type.add(String.format("%s %s", "|----", four_key));
                    }
                }
            }
        }
        ehcacheUtil.addKey("App_init", "zzfl", type);
    }

    /**
     * 初始化同花顺分类
     */
    void init_ths_type() {
        List<String> type = new ArrayList<>();
        JSONArray lvfours = mongoTemplate.groupBy(null, "stock", "industry").getJSONArray("retval");
        for (int l = 0; l < lvfours.size(); l++) {
            JSONObject lvfour_row = lvfours.getJSONObject(l);
            String industry = lvfour_row.getString("industry");
            if (industry.trim().isEmpty()) {
                continue;
            }
            type.add(industry);
        }
        ehcacheUtil.addKey("App_init", "ths_type", type);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
