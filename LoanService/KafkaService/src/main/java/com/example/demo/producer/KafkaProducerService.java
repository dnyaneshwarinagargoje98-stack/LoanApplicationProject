package com.example.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private static final String TOPIC = "test-loanapp";
	
	public void sendMessage(String msg) {
	kafkaTemplate.send(TOPIC, msg);
	System.out.println(" kafka message : " + msg);
		
	}

}
