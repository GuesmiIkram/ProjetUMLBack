package com.example.projet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors() // utilise ta CorsConfig
            .and()
            .csrf().disable() // désactive CSRF pour Angular
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // autorise tous les endpoints /api pour le dev
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
