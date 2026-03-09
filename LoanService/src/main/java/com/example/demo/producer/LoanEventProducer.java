package com.example.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.LoanEvent;

@Service
public class LoanEventProducer {
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	public void publishLoanEvent(LoanEvent event) {
		
		kafkaTemplate.send("loan-events", event);
		
		System.out.println("Loan event published to kafka ");
		
	}

}
