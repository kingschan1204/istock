package io.github.kingschan1204.istock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@SpringBootApplication
public class Application {


    @RequestMapping("/")
    public String index() {
        return "index";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}