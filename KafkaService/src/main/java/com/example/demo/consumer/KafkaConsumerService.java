package com.example.demo.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

	@KafkaListener(topics = "app-topic",groupId = "my-group")
	public void consume(String msg) {
		System.out.println(" consume message : "+ msg);
	}
}
