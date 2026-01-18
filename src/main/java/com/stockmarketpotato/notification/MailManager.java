package com.stockmarketpotato.notification;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.stockmarketpotato.configuration.Settings;
import com.stockmarketpotato.configuration.SettingsRepository;
import com.stockmarketpotato.security.UserRepository;

import j2html.tags.Tag;

@Component
public class MailManager {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SettingsRepository settings;
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
	protected SpringTemplateEngine templateEngine;
    
    public void sendEmailToUser(final String text, final String subject) {
    	sendEmailToUser(text, subject, false);
    }

	public void sendEmailToUser(final String text, final String subject, final boolean htmlText) {

        Settings s = settings.findAll().get(0);
        if (!s.isMailEnabled())
        	return;
        Properties props = new Properties();
        props.put("mail.smtp.auth", s.getMailSmtpAuth()); //Enabling SMTP Authentication
        props.put("mail.host", s.getMailHost());
        props.put("mail.smtp.port", s.getMailSmtpPort());
        props.put("mail.smtp.ssl.enable", s.getMailSmtpSslEnable());
        props.put("mail.transport.protocol", s.getMailTransportProtocol());
        
        final String username = s.getMailUsername();
        final String password = s.getMailPassword();
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        // Set debug on the Session so we can see what is going on
        // Passing false will not echo debug info, and passing true
        // will.
        MimeMessage message = new MimeMessage(session);
        try {
        	final String receiver = userRepository.findAll().get(0).getEmail();
			message.setFrom(new InternetAddress(username));
        	message.setRecipient(RecipientType.TO, new InternetAddress(receiver));
	        message.setSubject(subject);
	        if (htmlText)
	        	message.setText(text, "UTF-8", "html"); // as "text/plain"
	        else
	        	message.setText(text, "UTF-8"); // as "text/plain"
	        message.setSentDate(new Date());
	        Transport.send(message);
	        log.info("Email sent.");
        } catch (MessagingException e) {
        	log.error(e.getMessage());
		}
	}

	public void sendEmailToUser(String template, String subject, Tag<?> htmlTag) {
		Context thymeleafContext = new Context();
	    thymeleafContext.setVariable("message", htmlTag.render());
	    String htmlBody = templateEngine.process(template, thymeleafContext);
	    sendEmailToUser(htmlBody, subject, true);
	}
}