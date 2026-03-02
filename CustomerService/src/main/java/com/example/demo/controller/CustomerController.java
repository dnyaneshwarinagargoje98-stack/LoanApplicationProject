package com.example.demo.controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.ForgotPasswordRequest;
import com.example.demo.dto.ResetPasswordRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	  private static final Logger log = LogManager.getLogger(CustomerController.class);
	           
	@Autowired
	private CustomerService service;

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> registerUserInfo(@RequestBody User user) {
		log.info("Customer registration started");
		UserResponse userResponse = service.registerUserInfo(user);
		log.info("Customer registration completed successfully");
		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}

	@GetMapping("/getUserByEmail/{email}")
	public ResponseEntity<?> getUserDetailsByEmail(@PathVariable String email) {
		log.info("Fetching customer by email: {}", email);
		ResponseEntity<?> response = service.getUserDetailsByEmailId(email);
		log.info("Fetching customer by email completed");
		return response;
	}

	@GetMapping("/getUserById/{id}")
	public ResponseEntity<?> getUserDetailsById(@PathVariable int id) {
		log.info("Fetching customer by ID: {}", id);
		ResponseEntity<?> response = service.getUserDetailsById(id);
		log.info("Fetching customer by ID completed");
		return response;
	}

	@GetMapping("/getUserByUsername/{username}")
	public ResponseEntity<?> getUserDetailsByUsername(@PathVariable String username) {
		log.info("Fetching customer by username: {}", username);
		ResponseEntity<?> response = service.getUserDetailsByUsername(username);
		log.info("Fetching customer by username completed");
		return response;
	}

	@PutMapping("/updateUser")
	public ResponseEntity<User> updateUserDetails(@RequestBody User user) {
		log.info("Updating user details");
		log.debug("Update request body: {}", user);
		User updatedUser = service.updateUserDetails(user);
		log.info("User updated successfully");
		return ResponseEntity.ok(updatedUser);
	}
	
	@DeleteMapping("/deleteUser/{email}")
	public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
		log.info("Controller delete User by email start");
		ResponseEntity<?> response = service.deleteUserByEmail(email);
		log.info("Controller delete user by email end");
		return response;
	}
	
	@PutMapping("/softDeleteUser/{email}")
	public ResponseEntity<?> disableUser(@PathVariable String email) {
		log.info("soft delete user request received for email: {}", email);
		return service.softDeleteUser(email);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
		log.info("Controller Forgot password start");
		return service.forgotPassword(request.getEmail(), request.getNewPassword());
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
		log.info("Reset password request received for email: {}", request.getEmail());
		return service.resetPassword(request.getEmail(), request.getOldPassword(), request.getNewPassword());
	}

	@PostMapping("/assign-role/{username}/{roleName}")
	public ResponseEntity<?> assignRole(@PathVariable("username") String username, @PathVariable("roleName") String roleName) {
		log.info("Controller assigne role start");
		return service.assignRoleToUser(username, roleName);
	}
	
	@GetMapping("/paging-users")
	public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "username,asc") String sort) {
		String[] sortParams = sort.split(",");
		Sort sorting = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
		Pageable pageable = PageRequest.of(page, size, sorting);
		Page<User> userPage = service.getAllUsers(pageable);
		Page<UserResponse> response = userPage.map(user -> {
			UserResponse dto = new UserResponse();
			dto.setUsername(user.getUsername());
			dto.setMessage("User fetched");
			return dto;
		});
		return ResponseEntity.ok(response);
	}

}
