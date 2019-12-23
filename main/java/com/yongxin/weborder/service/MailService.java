package com.yongxin.weborder.service;

import org.springframework.mail.MailException;

public interface MailService
{
    /**
     * 发送邮件提醒
     * @param to
     * @param subject
     * @param content
     * @throws MailException
     */
    void sendSimpleMail(String to, String subject, String content) throws MailException;
}
