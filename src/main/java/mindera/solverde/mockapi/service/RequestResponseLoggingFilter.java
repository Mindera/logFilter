package mindera.solverde.mockapi.service;

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

        ObjectMapper mapper = new ObjectMapper();
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
                        req.getHeader("query"),
                        new Header(
                                req.getHeader("content-length"),
                                req.getHeader("user-agent"),
                                req.getHeader("accept-encoding"),
                                req.getHeader("accept"),
                                req.getHeader("host"),
                                req.getHeader("connection"),
                                req.getContentType())));
        log.setResponseTime(res.getHeader("response-time"));

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(System.out, log);
        mapper.writeValue(new File("log.json"), log);


//        String filteredResponse = new String(
//                "response: " + responseStr + "\n" +
//                "date: " + res.getHeader("Date") + "\n" +
//                "service: " + res.getHeader("service") + "\n" +
//                "environment: " + res.getHeader("environment") + "\n" +
//                "request: {\n" +
//                        "body: "+ requestString + "\n" +
//                "method: " + req.getMethod() + "\n" +
//                "path: " + req.getRequestURI() + "\n" +
//                "status: " + res.getStatus() + "\n" +
//                "query: " + req.getQueryString() + "\n" +
//                "headers : {\n" +
//                        "content-length: " + req.getHeader("content-length") + "\n" +
//                        "user-agent: " + req.getHeader("user-agent") + "\n" +
//                        "accept-encoding: " + req.getHeader("accept-encoding") + "\n" +
//                        "accept: " + req.getHeader("accept") + "\n" +
//                        "host: " + req.getHeader("host") + "\n" +
//                        "connection: " + req.getHeader("connection") + "\n" +
//                        "content-type: " + req.getHeader("content-type") + "\n" +
//                        "}\n" + "}\n" +
//                "message: " + req.getHeader("message"));
//
//
//        System.out.println(filteredResponse);

//        System.out.println("responseBody: " + responseStr);
//        System.out.println("date: " + res.getHeader("Date"));
//        System.out.println("service" + res.getHeader("service"));
//        System.out.println("environment" + res.getHeader("environment"));
//        System.out.println("requestBody: " + requestString);
//        System.out.println("method:" + req.getMethod());
//        System.out.println("URI: " + req.getRequestURI());
//        System.out.println("response: " + res.getStatus());
//        System.out.println();
//        System.out.println("Content Type: " + res.getContentType());
//        System.out.println("Header Fields: " + res.getHeaderNames());
    }
}
