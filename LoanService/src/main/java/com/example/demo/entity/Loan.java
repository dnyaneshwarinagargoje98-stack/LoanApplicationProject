package com.example.demo.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanId;
	
	private Long customerId;
	
	private Double loanAmount;
	
	private Integer tenureMonths;
	
	private Double interestRate;
	
	private Double emiAmount;
	
	private String loanType;
	
	private String status;
	
	private LocalDate appliedDate;
	
	private LocalDate approvalDate;
	
	@Column(nullable = false)
	private Boolean deleted;	
	
}
