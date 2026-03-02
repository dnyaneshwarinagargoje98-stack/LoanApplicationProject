package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Integer>{
	
	@Query(value = "select username from login" , nativeQuery = true)
	List<String> findByAllUsernames();
	
	Login findById(int id);
	Login findByUsername(String username);

}
