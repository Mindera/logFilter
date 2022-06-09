package mindera.logfilter.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import mindera.logfilter.models.Log;
import mindera.logfilter.models.Request;
import mindera.logfilter.models.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


public class RequestResponseLoggingFilter implements Filter {

    private final ObjectMapper objectMapper;


    public RequestResponseLoggingFilter() {
        objectMapper = new ObjectMapper()
//                .enable(SerializationFeature.INDENT_OUTPUT)
                .configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        ResponseWrapper wrappedResp = new ResponseWrapper((HttpServletResponse) servletResponse);


        long start = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, wrappedResp);
        long end = System.currentTimeMillis() - start;

        byte[] bytes = wrappedResp.getBaos().toByteArray();
        String responseStr = new String(bytes);
        servletResponse.getOutputStream().write(bytes);

        String requestBody = requestWrapper.getBody();
        String responseBody = responseStr;

        generateLog(requestWrapper, wrappedResp, requestBody, responseBody, end);
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
        log.setResponseTime(String.valueOf(time) + " ms \n");

        objectMapper.writeValue(System.out, log);
    }

}

