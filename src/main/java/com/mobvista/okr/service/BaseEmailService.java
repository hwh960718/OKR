package com.mobvista.okr.service;

import com.mobvista.okr.config.EmailProperties;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * 描述：
 *
 * @author Weier Gu (顾炜)
 * @date 2018/10/15 13:55
 */
@Service
public class BaseEmailService {


    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(BaseEmailService.class);

    private final SpringTemplateEngine templateEngine;

    @Resource
    private EmailProperties emailProperties;

    public BaseEmailService(SpringTemplateEngine engine) {
        templateEngine = engine;
    }


    /**
     * 简单的邮件发送功能
     *
     * @param context      邮件内容
     * @param toEmail      邮件接收人
     * @param subject      邮件主题
     * @param templateName 模板名称
     */
    public void sendEmailSimple(Context context, String toEmail, String subject, String templateName) {
        sendEmailSimple(context, new String[]{toEmail}, subject, templateName);
    }

    /**
     * 简单的邮件发送功能
     *
     * @param context      邮件内容
     * @param toEmail      邮件接收人(多个)
     * @param subject      邮件主题
     * @param templateName 模板名称
     */
    public void sendEmailSimple(Context context, String[] toEmail, String subject, String templateName) {
        Transport transport = null;
        try {
            Session session = getSession();
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, CharEncoding.UTF_8);
            message.setFrom(new InternetAddress(emailProperties.getSender(), emailProperties.getSenderName()));
            message.setSubject(subject);
            if (emailProperties.isTest()) {
                message.setTo(emailProperties.getReceiveMailAddress());
            } else {
                message.setTo(toEmail);
            }

            if (null == toEmail || toEmail.length == 0) {
                log.error("{}-->邮件发送，邮件接收人不存在", subject);
                return;
            }

            String content = templateEngine.process(templateName, context);

            Multipart multipart = new MimeMultipart();
            BodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(htmlPart);
            mimeMessage.setContent(multipart);
            log.info("{}-->邮件发送，邮件开关：{}", subject, emailProperties.isEmailEnable());
            if (emailProperties.isEmailEnable()) {
                transport = session.getTransport();
                //使用stmp 用户名、密码
                //transport.connect(emailProperties.getHost(), emailProperties.getUserName(), emailProperties.getPassword());
                //发送邮件
                //transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            }
            log.info("{}邮件提醒发送成功，\n 邮件接收人：{} \n 邮件实际接收人：{}", subject, toEmail, mimeMessage.getAllRecipients());
        } catch (Exception e) {
            log.error("{}邮件提醒异常，邮件接收人：{}", subject, toEmail, e);
        } finally {
            try {
                if (null != transport) {
                    transport.close();
                }
            } catch (MessagingException e) {

            }
        }
    }


    /**
     * 获取session
     *
     * @return
     */
    private Session getSession() {
        //根据配置文件获取session
        Session session = Session.getDefaultInstance(getProperties());
        return session;
    }

    public Properties getProperties() {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", emailProperties.getPort());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        return props;
    }
}
