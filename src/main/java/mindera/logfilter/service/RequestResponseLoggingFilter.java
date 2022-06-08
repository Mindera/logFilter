package mindera.logfilter.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import mindera.logfilter.models.Request;
import mindera.logfilter.models.Log;
import mindera.logfilter.models.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
//
public class RequestResponseLoggingFilter implements Filter {

    private final ObjectMapper objectMapper;

    public RequestResponseLoggingFilter() {
        objectMapper = new ObjectMapper().configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        SpringlessContentCachingRequestWrapper requestWrapper = new SpringlessContentCachingRequestWrapper((HttpServletRequest) request);
        SpringlessContentCachingResponseWrapper responseWrapper = new SpringlessContentCachingResponseWrapper((HttpServletResponse) response);

        long start = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long time = System.currentTimeMillis() - start;

        byte[] requestArray = requestWrapper.getContentAsByteArray();
        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String requestBody = new String(requestArray, request.getCharacterEncoding());
        String responseBody = new String(responseArray, response.getCharacterEncoding());

        responseWrapper.copyBodyToResponse();

        generateLog(requestWrapper, responseWrapper, requestBody, responseBody, time);

    }

    public void generateLog(HttpServletRequest req, HttpServletResponse res, String requestString, String responseStr, long time) throws IOException {

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
        log.setResponseTime(time + " ms\n");

        objectMapper.writeValue(System.out, log);
    }
}

