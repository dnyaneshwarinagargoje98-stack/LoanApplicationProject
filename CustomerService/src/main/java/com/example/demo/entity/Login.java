package com.example.demo.entity;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import com.example.demo.dto.LoginDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Login {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String username;
	
	private String email;
	
	private String password;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JsonBackReference
	private User user;
	
	private String rolename;
	
	public  LoginDto convertLoginToLoginDto(Login login) {
	    if (login == null) {
	        return null;
	    }return LoginDto.builder().id(login.getId()).username(login.getUsername()).email(login.getEmail()).build();
	}
}
