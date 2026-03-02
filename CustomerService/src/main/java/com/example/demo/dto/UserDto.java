package com.example.demo.dto;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value =Include.NON_NULL )
public class UserDto {
	
	@JsonInclude(value =Include.NON_EMPTY)
	private int id;

	private String firstname;

	private String lastname;

	private String address;

	private String username;

	private String password;
	
	private String email;

	private String mobileNo;

	private String dob;

	private String country;

	private String state;

	private String city;

	private String zipcode;

	@OneToOne(cascade = CascadeType.ALL)
	private LoginDto loginDto;

}
