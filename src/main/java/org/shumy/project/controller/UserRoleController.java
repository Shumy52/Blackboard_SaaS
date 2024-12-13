package org.shumy.project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserRoleController {

    @GetMapping("/user-role")
    public Map<String, String> getUserRole(Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                response.put("role", "ADMIN");
                return response;
            }
        }
        response.put("role", "USER");
        return response;
    }
}