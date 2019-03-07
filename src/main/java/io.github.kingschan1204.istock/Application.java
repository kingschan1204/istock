package io.github.kingschan1204.istock;

import io.github.kingschan1204.istock.common.startup.InitQuartzTaskRunner;
import io.github.kingschan1204.istock.module.maindata.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * spring boot 启动类
 * @author kings.chan
 */
@Controller
@EnableCaching
@ServletComponentScan
@SpringBootApplication
public class Application {

    @Autowired
    private StockService stockService;

    @RequestMapping("/")
    public String index(Model model) {
        List<String> list =stockService.getAllIntruduce();
        model.addAttribute("industry",list);
        return "index";
    }

//    @Bean
    public InitQuartzTaskRunner startupRunner() {
        return new InitQuartzTaskRunner();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}