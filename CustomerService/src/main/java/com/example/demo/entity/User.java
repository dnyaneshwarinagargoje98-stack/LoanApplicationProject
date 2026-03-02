package com.example.demo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import com.example.demo.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String firstname;
	
	@Column(nullable = false)
	private String lastname;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false,unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String email;
	
	private String mobileNo;
	
	private String dob;
	
	private String createdBy;
	
	private String createdDate;
	
	private String upadatedBy;
	
	private String updatedDate;
	
	private String country;
	
	private String state;
	
	private String city;
	
	private String zipcode;
	
	private boolean status;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JsonManagedReference
	private Login login;
	
	public UserDto convertUserToUserdto(User user) {
		return UserDto.builder().id(user.getId()).firstname(user.getFirstname()).lastname(user.getLastname()).address(user.getAddress())
				.username(user.getUsername()).email(user.getEmail()).mobileNo(user.getMobileNo()).dob(user.getDob()).country(user.getCountry()).state(user.getState())
				.city(user.getCity()).zipcode(user.getZipcode()).loginDto(login.convertLoginToLoginDto(user.getLogin())).build();
	}

	
}
