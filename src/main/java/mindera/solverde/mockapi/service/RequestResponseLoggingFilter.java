package mindera.solverde.mockapi.service;

import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class RequestResponseLoggingFilter implements Filter {


    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        System.out.println("Method:" + req.getMethod());
        System.out.println("URI: " + req.getRequestURI());

        chain.doFilter(request, response);

        System.out.println("Content Type: " + res.getContentType());
    }
    // other methods
}
