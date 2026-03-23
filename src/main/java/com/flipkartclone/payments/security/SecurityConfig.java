package com.flipkartclone.payments.security;

import com.flipkartclone.payments.service.HmacSha256Service;
import com.flipkartclone.payments.util.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private final HmacSha256Service hmacSha256Service;
    private final JsonUtil jsonUtil;

    public SecurityConfig(HmacSha256Service hmacSha256Service, JsonUtil jsonUtil) {
        this.hmacSha256Service = hmacSha256Service;
        this.jsonUtil = jsonUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                       .anyRequest().permitAll()
//               )

               .authorizeHttpRequests(auth -> auth
                       .anyRequest().authenticated()
               )
               .addFilterBefore(new ExceptionHandlerFilter(jsonUtil), DisableEncodeUrlFilter.class)
               .addFilterAfter(new HmacSha256CustomFilter(hmacSha256Service), LogoutFilter.class)


                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

}
