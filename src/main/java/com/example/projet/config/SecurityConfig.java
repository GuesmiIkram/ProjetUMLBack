package com.example.projet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())   // active CORS
            .csrf(csrf -> csrf.disable())      // désactive CSRF pour Angular

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/**").permitAll()   // autorise les APIs
                    .anyRequest().permitAll()
            );

        return http.build();
    }
}