package io.github.kingschan1204.istock;

import io.github.kingschan1204.istock.module.startup.InitQuartzTaskRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.*;

/**
 * spring boot 启动类
 * @author kings.chan
 */
@Controller
@EnableCaching
@SpringBootApplication
public class Application {


    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @Bean
    public InitQuartzTaskRunner startupRunner() {
        return new InitQuartzTaskRunner();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}