package com.example.demo.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.dto.CreateRoleRequest;
import com.example.demo.entity.Role;
import com.example.demo.repository.AdminRepository;



@Service
public class AdminServiceImpl implements AdminService {

	private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private AdminRepository adminRepo;

	@Override
	public ResponseEntity<?> createRole(CreateRoleRequest request) {
		log.info("Creating role: {}", request.getRoleName());
		if (adminRepo.existsByRoleName(request.getRoleName())) {
			log.warn("Role already exists: {}", request.getRoleName());
			return new ResponseEntity<>("Role already exists", HttpStatus.BAD_REQUEST);
		}

		Role role = new Role();
		role.setRoleName(request.getRoleName());
		role.setActive(true);
		adminRepo.save(role);
		log.info("Role created successfully: {}", request.getRoleName());
		return new ResponseEntity<>("Role created successfully", HttpStatus.CREATED);
	}

	@Override
	public List<Role> findAllRoles() {
		log.info("Fetching all roles from database");
		List<Role> roles = adminRepo.findAll();
		if (roles.isEmpty()) {
			log.warn("No roles found in database");
		} else {
			log.info("Total roles fetched: {}", roles.size());
		}
		return roles;
	}

	@Override
	public ResponseEntity<?> updateRole(String roleName, Role role) {
		Role existingRole = adminRepo.findByRoleName(roleName);
		if (existingRole == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
		}
		existingRole.setRoleName(role.getRoleName());
		adminRepo.save(existingRole);
		return ResponseEntity.status(HttpStatus.OK).body(existingRole);
	}

	@Override
	public ResponseEntity<?> deleterole(String roleName) {
		Role existingRole = adminRepo.findByRoleName(roleName);
		if (existingRole == null) {
			return new ResponseEntity<>("Role not found", HttpStatus.OK);
		}
		adminRepo.delete(existingRole);
		return new ResponseEntity<>("Role delete successfully ", HttpStatus.OK);
	}

}
