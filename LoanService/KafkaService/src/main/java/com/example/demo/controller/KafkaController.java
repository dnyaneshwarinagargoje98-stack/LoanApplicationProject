package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.producer.KafkaProducerService;

@RestController
@RequestMapping("/api")
public class KafkaController {

	@Autowired
	private KafkaProducerService kafkaProducerService;
	
	@GetMapping("/message")
	public String sendMessage(@RequestParam String msg) {
		kafkaProducerService.sendMessage(msg);
		return"Message sent to kafka " + msg ;
	}
}
