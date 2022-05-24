package mindera.solverde.mockapi.service;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

@Component
@Order(1)
public class RequestResponseLoggingFilter implements Filter {


    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, responseWrapper);

        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String responseStr = new String(responseArray, response.getCharacterEncoding());

        responseWrapper.copyBodyToResponse();



        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        System.out.println("Method:" + req.getMethod());
        System.out.println("URI: " + req.getRequestURI());
        System.out.println("Response: " + res.getStatus());
        System.out.println("Content Type: " + res.getContentType());
        System.out.println("Header Fields: " + res.getHeaderNames());
        System.out.println("Date: " + res.getHeader("Date"));
        System.out.println("response Str: " + responseStr);
    }

    // other methods
}
