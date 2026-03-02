package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.example.demo.dto.CreateRoleRequest;
import com.example.demo.entity.Role;


public interface AdminService {

	ResponseEntity<?> createRole(CreateRoleRequest request);
	List<Role> findAllRoles();
	ResponseEntity<?> updateRole(String roleName, Role role);
	ResponseEntity<?> deleterole(String roleName);
}
