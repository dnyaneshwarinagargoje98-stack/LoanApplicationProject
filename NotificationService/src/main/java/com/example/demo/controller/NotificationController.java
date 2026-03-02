package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.EmailRequest;
import com.example.demo.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

	private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	private NotificationService service;

	@PostMapping("/email")
	public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
		log.info("Email notification request received for: {}", request.getTo());
		service.sendEmail(request);
		log.info("Email sent successfully");
		return ResponseEntity.ok("Email sent successfully");
	}

}
