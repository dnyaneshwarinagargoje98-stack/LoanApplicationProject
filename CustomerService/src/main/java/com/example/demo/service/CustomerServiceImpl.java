package com.example.demo.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.client.RoleClient;
import com.example.demo.controller.CustomerController;
import com.example.demo.dto.EmailRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.Login;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enumValue.EnumData;
import com.example.demo.exceptionHandling.InvalidPasswordException;
import com.example.demo.exceptionHandling.RoleNotFoundException;
import com.example.demo.exceptionHandling.UserNotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.LoginRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	Logger log = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private LoginRepository loginRepo;

	@Autowired
	private RoleClient roleClient;

	@Override
	public UserResponse registerUserInfo(User user) {
		log.info("In customer service start!!");
		boolean isExist = getAlreadyRegisterdEmployeeData(user.getEmail());
		UserResponse userResponse = new UserResponse();
		if (isExist) {
			userResponse.setUsername(user.getEmail());
			userResponse.setMessage("User Email Already Exist.");
			return userResponse;
		}

		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String strDate = formatter.format(date);
		user.setCreatedDate(strDate);

		boolean flag = false;
		EnumData status = EnumData.ACTIVE;
		if ("A".equals(status.getValue())) {
			flag = true;
		}
		user.setStatus(flag);
		user.setCreatedBy(user.getUsername());

		Login login = new Login();
		login.setUsername(user.getUsername());
		login.setEmail(user.getEmail());
		login.setPassword(user.getPassword());
		login.setUser(user);
		user.setLogin(login);
		User user1 = customerRepo.save(user);
		if (user1 != null) {
			sendEmailNotification(user1);
			userResponse.setUsername(user1.getUsername());
			userResponse.setMessage("Thank You!! User is registered successfully!!");
		} else {
			userResponse.setMessage("Thank You!! User is not registered successfully!!");
		}
		log.info("In customer service end!!");
		return userResponse;
	}

	private boolean getAlreadyRegisterdEmployeeData(String email) {
		User user = customerRepo.findByEmail(email);
		if (user != null) {
			return true;
		}
		return false;
	}

	@Override
	public ResponseEntity<?> getUserDetailsByEmailId(String email) {
		log.info("In customer service  getting user Start!!");
		User user = customerRepo.findByEmail(email);
		if (user != null) {
			UserDto userDto2 = user.convertUserToUserdto(user);
			return new ResponseEntity<UserDto>(userDto2, HttpStatus.OK);

		}
		UserResponse userResponse = new UserResponse();
		userResponse.setUsername(email);
		userResponse.setMessage("Email Id not exists!!");
		log.info("In customer service  getting user end!!");
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getUserDetailsById(int id) {
		log.info("In customer service  getting user by id start!!");
		User user = customerRepo.findById(id);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		UserResponse userResponse = new UserResponse();
		userResponse.setMessage("User is  not exists!!");
		log.info("In customer service  getting user by id end!!");
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getUserDetailsByUsername(String username) {
		log.info("In customer service  getting user by username start!!");
		User user = customerRepo.findByUsername(username);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		UserResponse userResponse = new UserResponse();
		userResponse.setMessage("User is  not exists!!");
		log.info("In customer service  getting user by username end!!");
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@Override
	public User updateUserDetails(User user) {
		log.info("In customer service update user details start!!");
		User existingUser = customerRepo.findByEmail(user.getEmail());

		if (existingUser == null) {
			throw new UserNotFoundException("User not found ");
		}
		existingUser.setUsername(user.getUsername());
		existingUser.setMobileNo(user.getMobileNo());
		existingUser.setPassword(user.getPassword());
		User updatedUser = customerRepo.save(existingUser);
		log.info("In customer service update user details end!!");
		return updatedUser;
	}

	@Override
	public ResponseEntity<?> deleteUserByEmail(String email) {
		log.info("In Customer Service Delete Customer By Email Method");
		User user = customerRepo.findByEmail(email);
		boolean flag = true;
		if (user != null) {
			if (user.isStatus()) {
				EnumData status = EnumData.NONACTIVE;
				if ("N".equals(status.getValue())) {
					flag = false;
				}
				user.setStatus(flag);
				user.setUpadatedBy(user.getUsername());
				user.setUpdatedDate(new java.util.Date().toString());
				customerRepo.save(user);
				return new ResponseEntity<User>(user, HttpStatus.OK);
			} else {
				UserResponse response = new UserResponse();
				response.setUsername(email);
				response.setMessage("Customer Status is already NonActive Cannot proceed.");

				return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
			}
		}
		UserResponse response = new UserResponse();
		response.setUsername(email);
		response.setMessage("Customer not found with this email");
		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> softDeleteUser(String email) {
		System.out.println("Soft delete (disable) user process started");
		User user = customerRepo.findByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("User not found with email: " + email);
		}
		user.setStatus(false);
		customerRepo.save(user);
		return new ResponseEntity<>("User disabled successfully", HttpStatus.OK);

	}

	public void sendEmailNotification(User user) {
		String url = "http://localhost:9002/api/notification/email";
		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setTo(user.getEmail());
		emailRequest.setSubject("Registration Successful");
		emailRequest.setBody("Welcome " + user.getUsername() + ", your registration is successful.");
		log.info("Calling NotificationService for email");
		restTemplate.postForEntity(url, emailRequest, String.class);
		log.info("NotificationService call completed");
	}

	@Override
	public ResponseEntity<?> forgotPassword(String email, String newPassword) {
		log.info("Forgot password process started");
		User user = customerRepo.findByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("User not found with email: " + email);
		}
		user.setPassword(newPassword);
		customerRepo.save(user);
		log.info("Password updated successfully");
		return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> resetPassword(String email, String oldPassword, String newPassword) {
		log.info("Reset password process started");
		User user = customerRepo.findByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("User not found with email: " + email);
		}
		 if (!user.getPassword().equals(oldPassword)) {
		        throw new InvalidPasswordException("Old password is incorrect");
		    }
		user.setPassword(newPassword);
		customerRepo.save(user);
		log.info("Reset password process end");
		return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> assignRoleToUser(String username, String roleName) {
		Login login = loginRepo.findByUsername(username);
		if (login == null) {
			throw new UserNotFoundException("User not found");
		}

//			String url = "http://localhost:9003/api/admin/getrole/" + roleName;
//			Role role = restTemplate.getForObject(url, Role.class);

		Role role = roleClient.getRoleName(roleName);

		if (role == null) {
			throw new RoleNotFoundException("Role not found" + roleName);
		}
		login.setRolename(role.getRoleName());
		loginRepo.save(login);
		return new ResponseEntity<>(login, HttpStatus.OK);

	}

	@Override
	public Page<User> getAllUsers(Pageable pageable) {
		return customerRepo.findAll(pageable);
	}

}
