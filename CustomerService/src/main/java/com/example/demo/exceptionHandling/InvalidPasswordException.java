package com.example.demo.exceptionHandling;

public class InvalidPasswordException  extends RuntimeException{

	public InvalidPasswordException(String msg) {
		super(msg);
	}
}
