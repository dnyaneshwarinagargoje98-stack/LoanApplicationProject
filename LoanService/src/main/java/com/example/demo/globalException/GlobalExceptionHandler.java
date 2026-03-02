package com.example.demo.globalException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(LoanNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleLoanNotFound(LoanNotFoundException ex) {

		log.error("LoanNotFoundException: {}", ex.getMessage());

		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("error", "Not Found");
		response.put("message", ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidLoanStateException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidLoanState(InvalidLoanStateException ex) {

		log.error("InvalidLoanStateException: {}", ex.getMessage());

		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("error", "Conflict");
		response.put("message", ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

		log.error("Unexpected exception occurred", ex);

		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("error", "Internal Server Error");
		response.put("message", "Something went wrong");

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
