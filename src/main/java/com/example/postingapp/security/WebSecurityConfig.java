package com.example.postingapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    // SecurityFilterChain is a filter chain that is responsible for all security processing
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/css/**").permitAll()  // Allowed URLs to access without login
                .anyRequest().authenticated()  // Other URLs require login
            )
            .formLogin((form) -> form
                .loginPage("/login")              // Login form URL
                .loginProcessingUrl("/login")     // Login form action URL
                .defaultSuccessUrl("/?loggedIn")  // Login success URL
                .failureUrl("/login?error")       // Login failure URL
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/login?loggedOut")  // Logout success URL
                .permitAll()
            );

        return http.build();
    }

    // Password encoder that is used to encode and verify passwords
    @Bean
    public PasswordEncoder passwordEncoder() {         
        return new BCryptPasswordEncoder();
    }
}

