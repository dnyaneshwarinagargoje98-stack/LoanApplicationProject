package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.EmiRequestDto;
import com.example.demo.entity.Loan;
import com.example.demo.globalException.InvalidLoanStateException;
import com.example.demo.globalException.LoanNotFoundException;
import com.example.demo.model.LoanEvent;
import com.example.demo.producer.LoanEventProducer;
import com.example.demo.repository.LoanRepository;


@Service
public class LoanServiceImpl implements LoanService {

	private static final Logger log = LogManager.getLogger(LoanServiceImpl.class);

	@Autowired
	private LoanRepository loanRepo;
	
	@Autowired
	private LoanEventProducer loanEventProducer;
	
	@Override
	public Loan applyLoan(Loan loan) {
		log.info("Applying loan for customerId: {}", loan.getCustomerId());
		log.debug("Loan request data: {}", loan);
		loan.setStatus("PENDING");
		loan.setAppliedDate(LocalDate.now());
		loan.setDeleted(false);

		double interestRate = getInterestRate(loan.getLoanType());
		loan.setInterestRate(interestRate);
		log.debug("Interest rate set: {}", interestRate);

		Double emi = calculateEmi(new EmiRequestDto(loan.getLoanAmount(), interestRate, loan.getTenureMonths()));
		loan.setEmiAmount(emi);

		Loan savedLoan = loanRepo.save(loan);
		log.info("Loan applied successfully with loanId: {}", savedLoan.getLoanId());

		return savedLoan;
	}

	@Override
	public Loan getLoanById(Long loanId) {
	    return loanRepo.findById(loanId)
	            .orElseThrow(() -> new LoanNotFoundException(
	                    "Loan not found with id: " + loanId));
	}
	
	@Override
	public List<Loan> getLoanByCustomer(Long customerId) {
		 log.info("Fetching loans for customerId: {}", customerId);

		    List<Loan> loans = loanRepo.findByCustomerIdAndDeletedFalse(customerId);

		    if (loans.isEmpty()) {
		        log.error("No loans found for customerId: {}", customerId);
		        throw new RuntimeException("No loans found for customerId: " + customerId);
		    }

		    log.info("Total loans found for customerId {}: {}", customerId, loans.size());
		    return loans;
	}

	@Override
	public List<Loan> getLoanStatus(String status) {
		log.info("Fetching loans with status: {}", status);

		List<Loan> loans = loanRepo.findByStatusAndDeletedFalse(status.toUpperCase());
		log.info("Total loans found: {}", loans.size());

		return loans;
	}

	@Override
	public Double calculateEmi(EmiRequestDto dto) {
		log.debug("Calculating EMI for loanAmount: {}, tenure: {}", dto.getLoanAmount(), dto.getTenureMonths());

		double principal = dto.getLoanAmount();
		double annualRate = dto.getInterestRate();
		int months = dto.getTenureMonths();

		double monthlyRate = annualRate / 12 / 100;

		Double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months))
				/ (Math.pow(1 + monthlyRate, months) - 1);

		log.debug("Calculated EMI: {}", emi);
		return emi;
	}

	@Override
	public Loan approveLoan(Long loanId) {
	    Loan loan = loanRepo.findById(loanId)
	            .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

	    if (!loan.getStatus().equals("PENDING")) {
	        throw new InvalidLoanStateException(
	                "Loan is not in PENDING state. Current state: " + loan.getStatus());
	    }

	    loan.setStatus("APPROVED");
	    loan.setApprovalDate(LocalDate.now());
	    
	    Loan savedLoan = loanRepo.save(loan);
	    
	    LoanEvent event = new LoanEvent(savedLoan.getLoanId(),
	    		savedLoan.getCustomerId(), 
	    		savedLoan.getLoanAmount(), "APPROVED");
	    
	    loanEventProducer.publishLoanEvent(event);
	    
	    return savedLoan;
	}
	
	@Override
	public Loan rejectLoan(Long loanId) {
		log.info("Rejecting loan with loanId: {}", loanId);

		Loan loan = loanRepo.findById(loanId).orElseThrow(() -> {
			log.error("Loan not found for rejection, loanId: {}", loanId);
			return new RuntimeException("Loan not found");
		});

		loan.setStatus("REJECTED");
		loan.setApprovalDate(LocalDate.now());

		log.info("Loan rejected successfully, loanId: {}", loanId);
		 Loan savedLoan = loanRepo.save(loan);
		 
		 LoanEvent event = new LoanEvent(savedLoan.getLoanId(),
		    		savedLoan.getCustomerId(), 
		    		savedLoan.getLoanAmount(), "REJECTED");
		    
		    loanEventProducer.publishLoanEvent(event);
		    
		 return savedLoan;
	}

	@Override
	public Loan softDeleteLoan(Long loanId) {
		log.info("Soft deleting loan with loanId: {}", loanId);

		Loan loan = loanRepo.findByLoanIdAndDeletedFalse(loanId).orElseThrow(() -> {
			log.error("Active loan not found for deletion, loanId: {}", loanId);
			return new RuntimeException("Active loan not found with id: " + loanId);
		});

		loan.setDeleted(true);
		loan.setStatus("DELETED");

		log.info("Loan soft deleted successfully, loanId: {}", loanId);
		return loanRepo.save(loan);
	}

	private double getInterestRate(String loanType) {
		log.debug("Fetching interest rate for loanType: {}", loanType);

		switch (loanType.toUpperCase()) {
		case "HOME":return 8.5;
		case "CAR":return 9.5;
		case "PERSONAL":return 12.0;
		case "GOLD":return 7.5;
		default:return 10.0;
			
		}
	}
}