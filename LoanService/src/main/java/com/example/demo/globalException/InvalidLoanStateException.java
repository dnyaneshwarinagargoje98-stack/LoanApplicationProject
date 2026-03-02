package com.example.demo.globalException;

public class InvalidLoanStateException extends RuntimeException  {
	private static final long serialVersionUID = 1L;
	public InvalidLoanStateException(String message) {
        super(message);
    }
}
