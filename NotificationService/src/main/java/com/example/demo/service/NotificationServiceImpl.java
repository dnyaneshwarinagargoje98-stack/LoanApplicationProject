package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.dto.EmailRequest;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendEmail(EmailRequest request) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(request.getTo());
			message.setSubject(request.getSubject());
			message.setText(request.getBody());
            mailSender.send(message);
			log.info("Email successfully sent to {}", request.getTo());
		} 
		catch (Exception e) {
			log.error("Failed to send email", e);
			throw e;
		}
	}
}
