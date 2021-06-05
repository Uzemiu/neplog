package cn.neptu.neplog.service;

import cn.neptu.neplog.config.MailConfig;

public interface MailService{

    void sendSimpleMail(String to, String subject, String content);

    void sendHtmlMail(String to, String subject, String content);

    void sendAttachmentsMail(String to, String subject, String content, String filePath);

    void testConnection();
}
