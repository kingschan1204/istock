package io.github.kingschan1204.istock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kingschan on 2017/3/9.
 */
@Controller
public class SpringmvcController {

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("name","kingschan");
        return "/freemarker/index";
    }
}
