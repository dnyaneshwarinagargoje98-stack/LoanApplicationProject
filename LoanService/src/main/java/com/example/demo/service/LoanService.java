package com.example.demo.service;
import java.util.List;

import com.example.demo.dto.EmiRequestDto;
import com.example.demo.entity.Loan;

public interface LoanService {

	Loan applyLoan(Loan loan);

	Loan getLoanById(Long loanId);

	List<Loan> getLoanByCustomer(Long customerId);

	List<Loan> getLoanStatus(String status);

	Double calculateEmi(EmiRequestDto request);

	Loan approveLoan(Long loanId);

	Loan rejectLoan(Long loanId);

	Loan softDeleteLoan(Long loanId);
}
