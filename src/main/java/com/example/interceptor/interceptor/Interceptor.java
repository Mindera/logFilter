package com.example.interceptor.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class Interceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept
            (HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        ClientHttpResponse response = execution.execute(request,body);
        response.getHeaders().add("header name", "header Value");

        return response;
    }

}
