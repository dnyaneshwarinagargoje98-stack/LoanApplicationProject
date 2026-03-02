package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.entity.Role;

@FeignClient(name = "ADMINSERVICE")
@Component
public interface RoleClient {

	@GetMapping("/api/admin/getrole/{roleName}")
	public Role getRoleName(@PathVariable String roleName);
}
