package com.flipkartclone.payments.security;

import com.flipkartclone.payments.constant.Constant;
import com.flipkartclone.payments.service.HmacSha256Service;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;



@Slf4j
public class HmacSha256CustomFilter extends OncePerRequestFilter {

    private final HmacSha256Service hmacSha256Service;

    public HmacSha256CustomFilter(HmacSha256Service hmacSha256Service) {
        this.hmacSha256Service = hmacSha256Service;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String signature = request.getHeader(Constant.HMAC_SIGNATURE);

        WrappedRequest wrappedRequest = new WrappedRequest(request);

        String rawJsonBody = wrappedRequest.getBody();
        hmacSha256Service.validateHmacSignature(rawJsonBody, signature);


        boolean isValid = true;
       if (isValid) {

           log.info("Our custom HMAC SHA256 filter is processing the request: {}", request.getRequestURI());

           SecurityContext context = SecurityContextHolder.createEmptyContext();
           Authentication authentication = new HmacAuthenticationToken(Constant.MERCHANT_ID, signature, Constant.ROLE_MERCHANT);
           context.setAuthentication(authentication);
           SecurityContextHolder.setContext(context);
       }

        filterChain.doFilter(wrappedRequest , response);
        log.info("Our custom HMAC SHA256 filter has finished processing the request: {}", request.getRequestURI());
    }

}
