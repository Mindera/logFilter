package mindera.solverde.mockapi.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import mindera.solverde.mockapi.models.Log;
import mindera.solverde.mockapi.models.Request;
import mindera.solverde.mockapi.models.Response;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(1)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public RequestResponseLoggingFilter() {
        objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//    }
//
//    @Override
//    public void doFilterInternal(
//            ServletRequest request,
//            ServletResponse response,
//            FilterChain chain) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);


        filterChain.doFilter(requestWrapper, responseWrapper);

        byte[] requestArray = requestWrapper.getContentAsByteArray();
        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String requestString = new String(requestArray, request.getCharacterEncoding());
        String responseStr = new String(responseArray, response.getCharacterEncoding());

        responseWrapper.copyBodyToResponse();


        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        generateLog(req, res, requestString, responseStr);


    }


    public void generateLog(HttpServletRequest req, HttpServletResponse res, String requestString, String responseStr) throws IOException {
        Log log = new Log();

        Map<String, String> headers = Collections.list(req.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, req::getHeader));


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
                        headers));
        log.setResponseTime(res.getHeader("response-time"));

//        objectMapper.writeValue(System.out, log);
        System.out.println(log);
    }

}
