package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

	List<Loan> findByCustomerIdAndDeletedFalse(Long customerId);
	  List<Loan> findByStatusAndDeletedFalse(String status);
	  Optional<Loan> findByLoanIdAndDeletedFalse(Long loanId);

}
