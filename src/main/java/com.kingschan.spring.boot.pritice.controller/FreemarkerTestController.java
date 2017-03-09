package com.kingschan.spring.boot.pritice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by kingschan on 2017/3/9.
 * spring boot 整合freemarker 练习
 */
@RequestMapping("/freemarker")
@Controller
public class FreemarkerTestController {

    @RequestMapping("/fmtest")
    public String fmtest(ModelMap model){
        model.put("title","freemarker测试");
        model.put("content","这是来自controller的内容");
        return "/freemarker/test/fm-test";
    }

}
