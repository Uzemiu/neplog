package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.config.MailConfig;
import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

@Slf4j
@Service("mailService")
public class MailServiceImpl implements MailService, InitializingBean {

    private final JavaMailSenderImpl mailSender;
    private final MailConfig mailConfig;

    public MailServiceImpl(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
        this.mailSender = new JavaMailSenderImpl();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender.setHost(mailConfig.getHost());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setProtocol(mailConfig.getProtocol());
        mailSender.setDefaultEncoding(mailConfig.getDefaultEncoding().toString());
        Properties properties = new Properties();
        properties.putAll(mailConfig.getProperties());
        mailSender.setJavaMailProperties(properties);
//        mailSender.testConnection();
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfig.getUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(mailConfig.getUsername());
            messageHelper.setTo(subject);
            message.setSubject(subject);
            messageHelper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送邮件失败", e);
        }
    }

    @Override
    public void testConnection() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setDefaultEncoding(mailConfig.getDefaultEncoding().toString());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());
        mailSender.setProtocol(mailConfig.getProtocol());
        mailSender.setPort(mailConfig.getPort());

        try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            log.error("测试邮件服务失败", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailConfig.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            //日志信息
        } catch (MessagingException e) {
            log.error("发送邮件失败", e);
        }
    }

}
