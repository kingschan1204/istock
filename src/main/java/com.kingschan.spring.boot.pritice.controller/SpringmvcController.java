package com.kingschan.spring.boot.pritice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kingschan on 2017/3/9.
 */
@Controller
public class SpringmvcController {

    @ResponseBody
    @RequestMapping("/sayhi")
    public String sayHellow(){
        return "hello world.";
    }
}
