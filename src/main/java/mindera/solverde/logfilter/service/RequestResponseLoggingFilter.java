package mindera.solverde.logfilter.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import mindera.solverde.logfilter.models.Log;
import mindera.solverde.logfilter.models.Request;
import mindera.solverde.logfilter.models.Response;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
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
public class RequestResponseLoggingFilter implements Filter {

    private final ObjectMapper objectMapper;

    public RequestResponseLoggingFilter() {
        objectMapper = new ObjectMapper()
//                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {


        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);


        filterChain.doFilter(requestWrapper, responseWrapper);

        byte[] requestArray = requestWrapper.getContentAsByteArray();
        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String requestString = new String(requestArray, request.getCharacterEncoding());
        String responseStr = new String(responseArray, response.getCharacterEncoding());

        responseWrapper.copyBodyToResponse();

        generateLog(requestWrapper, responseWrapper, requestString, responseStr);

    }
//
//    @Override
//    public void doFilter(ServletRequest request,
//                         ServletResponse response,
//                         FilterChain filterChain) throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String requestBody = getRequestBody(httpRequest);
//        String responseBody = getResponseBody(httpResponse);
//
//        generateLog(httpRequest, httpResponse, requestBody, responseBody);
//
//        filterChain.doFilter(request, response);
//    }
//
//    public String getRequestBody(HttpServletRequest httpRequest) throws IOException {
//        BufferedReader reader = httpRequest.getReader();
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//        return sb.toString();
//    }
//
//    public String getResponseBody(HttpServletResponse httpResponse) throws IOException {
//
//        return "hello from response";
//    }


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

        objectMapper.writeValue(System.out, log);
    }

}
