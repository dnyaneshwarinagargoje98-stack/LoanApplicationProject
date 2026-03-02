package com.example.demo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.EmiRequestDto;
import com.example.demo.entity.Loan;
import com.example.demo.service.LoanService;

@RestController
@RequestMapping("/loan")
public class LoanController {

	private static final Logger log = LogManager.getLogger(LoanController.class);

	@Autowired
	private LoanService loanService;

	@PostMapping("/applyLoan")
	public ResponseEntity<Loan> applyLoan(@RequestBody Loan loan) {
		log.info("Loan application request received");
		log.debug("Loan details: {}", loan);
		Loan appliedLoan = loanService.applyLoan(loan);
		log.info("Loan applied successfully with loanId: {}", appliedLoan.getLoanId());
		return ResponseEntity.status(HttpStatus.CREATED).body(appliedLoan);
	}

	@GetMapping("/getLoanById/{loanId}")
	public ResponseEntity<Loan> getLoan(@PathVariable Long loanId) {
		log.info("Fetching loan by loanId: {}", loanId);
		Loan loan = loanService.getLoanById(loanId);
		log.info("Loan fetched successfully for loanId: {}", loanId);
		return ResponseEntity.status(HttpStatus.OK).body(loan);
	}

	@GetMapping("/getLoanByCustomer/{customerId}")
	public ResponseEntity<List<Loan>> getLoanByCustomerId(@PathVariable Long customerId) {
	    log.info("Fetching loans for customerId: {}", customerId);
	    List<Loan> loans = loanService.getLoanByCustomer(customerId);
	    log.info("Loans fetched successfully for customerId: {}", customerId);
	    return ResponseEntity.ok(loans);
	}

	@GetMapping("/getLoanStatus/{status}")
	public ResponseEntity<List<Loan>> getLoanStatus(@PathVariable String status) {
		log.info("Fetching loans with status: {}", status);
		List<Loan> loans = loanService.getLoanStatus(status);
		log.info("Total loans found with status {} : {}", status, loans.size());
		return ResponseEntity.status(HttpStatus.OK).body(loans);
	}

	@PostMapping("/calculateEMI")
	public ResponseEntity<Double> calculateEmi(@RequestBody EmiRequestDto request) {
		log.info("EMI calculation request received");
		log.debug("EMI Request: {}", request);
		Double emi = loanService.calculateEmi(request);
		log.info("EMI calculated successfully: {}", emi);
		return ResponseEntity.ok(emi);
	}

	@PutMapping("/approveLoan/{loanId}")
	public ResponseEntity<Loan> approveLoan(@PathVariable Long loanId) {
		log.info("Loan approval request received for loanId: {}", loanId);
		Loan loan = loanService.approveLoan(loanId);
		log.info("Loan approved successfully for loanId: {}", loanId);
		return ResponseEntity.ok(loan);
	}

	@PutMapping("/rejectLoan/{loanId}")
	public ResponseEntity<Loan> rejectLoan(@PathVariable Long loanId) {
		log.info("Loan rejection request received for loanId: {}", loanId);
		Loan loan = loanService.rejectLoan(loanId);
		log.info("Loan rejected successfully for loanId: {}", loanId);
		return ResponseEntity.ok(loan);
	}

	@DeleteMapping("/softDelete/{loanId}")
	public ResponseEntity<String> softDeleteLoan(@PathVariable Long loanId) {
		log.info("Soft delete request received for loanId: {}", loanId);
		loanService.softDeleteLoan(loanId);
		log.info("Loan soft deleted successfully for loanId: {}", loanId);
		return ResponseEntity.ok("Loan soft deleted successfully");
	}
}