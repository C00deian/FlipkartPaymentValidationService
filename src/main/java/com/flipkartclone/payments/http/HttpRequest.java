package com.flipkartclone.payments.http;

import lombok.Builder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import lombok.Data;

@Builder
@Data
public class HttpRequest {
	
	private HttpMethod httpMethod;
	private String url;
	private HttpHeaders httpHeaders;
	private Object requestData;

}
