package io.github.kingschan1204.istock.common.util.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringMailSender {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送简单邮件方法
     * @param sendTo 发送给谁
     * @param title 标题
     * @param text 发送内容
     */
    public void sendSimpleTextMail(String sendTo, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendTo);//收信人
        message.setSubject(title);//主题
        message.setText(text);//内容
        message.setFrom(from);//发信人
        mailSender.send(message);
    }

}
