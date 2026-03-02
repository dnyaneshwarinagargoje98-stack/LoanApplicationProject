package com.example.demo.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;

public interface CustomerService {
	
	UserResponse registerUserInfo(User user);
	
	ResponseEntity<?> getUserDetailsByEmailId(String email);

	ResponseEntity<?> getUserDetailsById(int id);

	ResponseEntity<?> getUserDetailsByUsername(String username);
	
	User updateUserDetails(User user);
	
    void sendEmailNotification(User user);

	ResponseEntity<?> forgotPassword(String email, String newPassword);

	ResponseEntity<?> resetPassword(String email, String oldPassword, String newPassword);

	ResponseEntity<?> softDeleteUser(String email);

	ResponseEntity<?> assignRoleToUser(String username, String roleName);

	ResponseEntity<?> deleteUserByEmail(String email);

	Page<User> getAllUsers(Pageable pageable);

}
