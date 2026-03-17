package com.flipkartclone.payments.security;

import com.flipkartclone.payments.service.HmacSha256Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private  final HmacSha256Service hmacSha256Service;

    public SecurityConfig(HmacSha256Service hmacSha256Service) {
        this.hmacSha256Service = hmacSha256Service;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated())
                .addFilterAfter(new HmacSha256CustomFilter(hmacSha256Service), LogoutFilter.class)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );


        return http.build();
    }

}
