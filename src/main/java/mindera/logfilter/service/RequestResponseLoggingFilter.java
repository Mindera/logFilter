package mindera.logfilter.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import mindera.logfilter.models.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


public class RequestResponseLoggingFilter implements Filter {

    private final ObjectMapper objectMapper;

    private final Log log;

    public RequestResponseLoggingFilter() {
        this.objectMapper = new ObjectMapper().configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        this.log = new Log();
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        SpringlessContentCachingRequestWrapper requestWrapper = new SpringlessContentCachingRequestWrapper((HttpServletRequest) request);
        SpringlessContentCachingResponseWrapper responseWrapper = new SpringlessContentCachingResponseWrapper((HttpServletResponse) response);

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
            handleRequest(request, response, requestWrapper, responseWrapper, start);

        } catch (Exception e) {
            handleRequest(request, response, requestWrapper, responseWrapper, start, e.getLocalizedMessage());
            throw new ServletException(e);
        }


    }

    private void handleRequest(ServletRequest request, ServletResponse response, SpringlessContentCachingRequestWrapper requestWrapper, SpringlessContentCachingResponseWrapper responseWrapper, long start, String exceptionMessage) throws IOException {
        byte[] requestArray = requestWrapper.getContentAsByteArray();
        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String requestBody = new String(requestArray, request.getCharacterEncoding());
        String responseBody = new String(responseArray, response.getCharacterEncoding());
        responseWrapper.copyBodyToResponse();
        generateLog(requestWrapper, responseWrapper, requestBody, responseBody, start, exceptionMessage);
    }

    private void handleRequest(ServletRequest request, ServletResponse response, SpringlessContentCachingRequestWrapper requestWrapper, SpringlessContentCachingResponseWrapper responseWrapper, long start) throws IOException {
        byte[] requestArray = requestWrapper.getContentAsByteArray();
        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String requestBody = new String(requestArray, request.getCharacterEncoding());
        String responseBody = new String(responseArray, response.getCharacterEncoding());
        responseWrapper.copyBodyToResponse();
        int statusCode = responseWrapper.getStatus();
        generateLog(requestWrapper, responseWrapper, requestBody, responseBody, start, statusCode);
    }

    public void generateLog(HttpServletRequest req, HttpServletResponse res, String requestString, String responseStr, long start, int statusCode) throws IOException {
        Log log = new Log();
        setLogDetails(log, req, res, requestString);
        Map<String, String> responseHeader = res.getHeaderNames().stream().collect(Collectors.toMap(h -> h, res::getHeader, (existingValue, newValue) -> newValue));
        log.setResponse(new StandardResponse(responseStr, responseHeader, statusCode));
        log.setResponseTime(System.currentTimeMillis() - start + " ms");
        printLog(objectMapper, log);
    }
    public void generateLog(HttpServletRequest req, HttpServletResponse res, String requestString, String responseStr, long start, String exceptionMessage) throws IOException {
        Log log = new Log();
        setLogDetails(log, req, res, requestString);
        Map<String, String> responseHeader = res.getHeaderNames().stream().collect(Collectors.toMap(h -> h, res::getHeader, (existingValue, newValue) -> newValue));
        log.setResponse(new ErrorResponse(responseStr, responseHeader, exceptionMessage));
        log.setResponseTime(System.currentTimeMillis() - start + " ms");
        printLog(objectMapper, log);
    }

    public void setLogDetails(Log log, HttpServletRequest req, HttpServletResponse res, String requestString) {
        Map<String, String> requestHeader = Collections.list(req.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, req::getHeader, (existingValue, newValue) -> newValue));
        log.setRequest(new Request(requestString, req.getMethod(), req.getRequestURI(), req.getQueryString(), requestHeader));
        log.setDate(res.getHeader("Date"));
        log.setService(res.getHeader("Service"));
        log.setEnvironment(res.getHeader("Environment"));
    }

    private void printLog(ObjectMapper objectMapper, Log log) throws IOException {
        objectMapper.writeValue(System.out, log);
        System.out.printf("\n\n");
    }


    @Override
    public void destroy() {
    }
}