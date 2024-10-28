package com.example.shared_blackboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // Set up an in-memory user
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws-draw/**").permitAll() // Allow WebSocket connections without login
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                //TODO: This login sucks. We HAVE to do more than this.
                .httpBasic(basic -> basic // Basic authentication instead of form login
                        .realmName("Blackboard Realm") // Optional: Set a realm name
                )
                .csrf(csrf -> csrf.disable()) // Disable CSRF with new API style
                .build();
    }
}
