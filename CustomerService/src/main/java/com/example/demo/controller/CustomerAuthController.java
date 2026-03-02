package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.jwt.JwtUtil;

@RestController
@RequestMapping("/auth")
public class CustomerAuthController {
	
	 @Autowired
	    private JwtUtil jwtUtil;

	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

	        if (!"admin".equals(request.getUsername()) ||
	            !"admin123".equals(request.getPassword())) {

	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body("Invalid credentials");
	        }

	        String token = jwtUtil.generateToken(request.getUsername(), "ADMIN");

	        return ResponseEntity.ok(new JwtResponse(token));
	    }

}
