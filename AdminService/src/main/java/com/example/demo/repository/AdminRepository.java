package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Role;

@Repository
public interface AdminRepository extends JpaRepository<Role, Integer>{

	boolean existsByRoleName(String roleName);
	
	Role findByRoleName(String roleName);
	

}
