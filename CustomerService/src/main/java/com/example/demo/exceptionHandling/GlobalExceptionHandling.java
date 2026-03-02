package com.example.demo.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandling {
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
		ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(RoleNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<ErrorResponse> invalidPassword(InvalidPasswordException ex) {
		ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	// Fallback for any unhandled exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		ErrorResponse error = new ErrorResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
