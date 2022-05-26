package mindera.solverde.mockapi.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import mindera.solverde.mockapi.models.Header;
import mindera.solverde.mockapi.models.Log;
import mindera.solverde.mockapi.models.Request;
import mindera.solverde.mockapi.models.Response;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Component
@Order(1)
public class RequestResponseLoggingFilter implements Filter {

    private final ObjectMapper objectMapper;

    public RequestResponseLoggingFilter() {
        objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(requestWrapper, responseWrapper);

        byte[] requestArray = requestWrapper.getContentAsByteArray();
        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String requestString = new String(requestArray, request.getCharacterEncoding());
        String responseStr = new String(responseArray, response.getCharacterEncoding());

        responseWrapper.copyBodyToResponse();


        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Log log = new Log();

        log.setResponse(new Response(responseStr));
        log.setDate(res.getHeader("Date"));
        log.setService(res.getHeader("Service"));
        log.setEnvironment(res.getHeader("Environment"));
        log.setRequest(
                new Request(
                        requestString,
                        req.getMethod(),
                        req.getRequestURI(),
                        req.getQueryString(),
                        new Header(
                                req.getHeader("content-length"),
                                req.getHeader("user-agent"),
                                req.getHeader("accept-encoding"),
                                req.getHeader("accept"),
                                req.getHeader("host"),
                                req.getHeader("connection"),
                                req.getContentType())));

        log.setResponseTime(res.getHeader("response-time"));

        objectMapper.writeValue(System.out, log);

    }
}
