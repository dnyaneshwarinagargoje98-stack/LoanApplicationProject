package com.example.demo.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.CreateRoleRequest;
import com.example.demo.entity.Role;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private static final Logger log = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminRepository adminRepo;

	@PostMapping("/createRoles")
	public ResponseEntity<?> createRole(@RequestBody CreateRoleRequest request) {
		log.info("Create Role API called");
		ResponseEntity<?> response = adminService.createRole(request);
		log.info("Create Role API completed");
		return response;
	}

	@GetMapping("/findRoles")
	public ResponseEntity<List<Role>> findAllRoles() {
		log.info("Received request to fetch all roles");
		List<Role> roles = adminService.findAllRoles();
		log.info("Returning response for findAllRoles API");
		return ResponseEntity.ok(roles);
	}

	@PutMapping("/updateRole/{roleName}")
	public ResponseEntity<?> updateRole(@PathVariable String roleName, @RequestBody Role role) {
		return adminService.updateRole(roleName, role);
	}

	@DeleteMapping(value = "/deleteRole/{rolename}")
	public ResponseEntity<?> deleteRole(@PathVariable("rolename") String roleName) {
		return adminService.deleterole(roleName);
	}

	@GetMapping("/getrole/{roleName}")
	public Role getRole(@PathVariable String roleName) {
		return adminRepo.findByRoleName(roleName);
	}

}
