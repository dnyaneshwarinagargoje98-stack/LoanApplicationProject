package com.example.demo.dto;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
	private int id;

	private String username;

	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	private User user;

}
