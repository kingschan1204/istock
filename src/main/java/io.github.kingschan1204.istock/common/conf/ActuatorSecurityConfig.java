package io.github.kingschan1204.istock.common.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.StringUtils;

/**
 * 定制Actuator SpringSecurity
 * @author chenguoxiang
 * @create 2018-04-12 16:38
 **/
@Configuration
@EnableWebSecurity
public class ActuatorSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    Environment env;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String contextPath = env.getProperty("management.context-path");
        if(StringUtils.isEmpty(contextPath)) {
            contextPath = "";
        }http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/**"+contextPath+"/**").authenticated()
                .anyRequest().permitAll()
                .and().httpBasic();
    }
}
