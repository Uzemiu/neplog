package cn.neptu.neplog.service;

import cn.neptu.neplog.model.config.MailConfig;

public interface MailService extends ConfigService<MailConfig, Long>{

    void sendSimpleMail(String to, String subject, String content);

    void sendHtmlMail(String to, String subject, String content);

    void sendAttachmentsMail(String to, String subject, String content, String filePath);

    void testConnection();
}
