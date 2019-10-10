package io.github.kingschan1204.istock.module.maindata.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.kingschan1204.istock.common.db.MyMongoTemplate;
import io.github.kingschan1204.istock.common.db.Page;
import io.github.kingschan1204.istock.module.maindata.po.CsIndexIndustry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ${DESCRIPTION}
 *
 * @author chenguoxiang
 * @create 2019-10-10 16:19
 **/
@RequestMapping("/csindex")
@Controller
public class CsIndexCtrl {

    @Autowired
    private MyMongoTemplate mongoTemplate;

    @RequestMapping("/page")
    public String page(Integer page, Integer rows, String sidx, String sord) {
        return "/stock/csindex/index";
    }

    @ResponseBody
    @RequestMapping("/list")
    public JSONObject list(Integer page, Integer rows, String sidx, String sord) {
        Sort.Direction sortd = "asc".equalsIgnoreCase(sord) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page p = mongoTemplate.pageQuery(page, rows, CsIndexIndustry.class, sortd, sidx, null);
        JSONObject data = new JSONObject();
        data.put("rows", JSONArray.parseArray(JSON.toJSONString(p.getData())));
        //有多少页
        data.put("total", p.getTotalPage());
        // 总共有多少条记录
        data.put("records", p.getTotal());
        data.put("page", p.getPageIndex());
        return data;
    }
}
