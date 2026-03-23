package com.flipkartclone.payments.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

    @LoadBalanced
    @Bean
    RestClient.Builder restClientBuilder(RestClientBuilderConfigurer configurer){
        return configurer.configure(RestClient.builder());
    }

    @Bean
    RestClient restClient(RestClient.Builder builder){
        return builder.build();
    }



}
