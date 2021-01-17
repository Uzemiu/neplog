package cn.neptu.neplog.service.impl;

import cn.neptu.neplog.exception.BadRequestException;
import cn.neptu.neplog.model.property.MailProperty;
import cn.neptu.neplog.service.MailService;
import cn.neptu.neplog.service.PropertyService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service("mailService")
public class MailServiceImpl implements MailService {

    private final PropertyService propertyService;
    private final JavaMailSenderImpl mailSender;

    private MailProperty mailProperty;

    public MailServiceImpl(PropertyService propertyService) {
        this.propertyService = propertyService;
        this.mailSender = new JavaMailSenderImpl();
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperty.getFrom());
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
            messageHelper.setFrom(mailProperty.getFrom());
            messageHelper.setTo(subject);
            message.setSubject(subject);
            messageHelper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
        }
    }

    @Override
    public void testConnection() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        MailProperty mailProperty = propertyService.getMailProperty();
        mailSender.setHost(mailProperty.getHost());
        mailSender.setDefaultEncoding(mailProperty.getEncoding());
        mailSender.setUsername(mailProperty.getUsername());
        mailSender.setPassword(mailProperty.getPassword());
        mailSender.setProtocol(mailProperty.getProtocol());
        mailSender.setPort(mailProperty.getPort());

        try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperty.getFrom());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            //日志信息
        } catch (MessagingException e) {
        }
    }
}
