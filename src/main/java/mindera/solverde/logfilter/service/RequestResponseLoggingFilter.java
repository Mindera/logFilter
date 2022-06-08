package mindera.solverde.logfilter.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import mindera.solverde.logfilter.models.Log;
import mindera.solverde.logfilter.models.Request;
import mindera.solverde.logfilter.models.Response;
import org.apache.commons.io.IOUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
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

  /*  @Override
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

    }*/

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteArrayPrintWriter pw = new ByteArrayPrintWriter(baos);

        HttpServletResponse wrappedResp = new HttpServletResponseWrapper((HttpServletResponse) servletResponse) {
            @Override
            public PrintWriter getWriter() {
                return pw;
            }

            @Override
            public ServletOutputStream getOutputStream() {
                return new ServletOutputStream() {
                    @Override
                    public boolean isReady() {
                        return false;
                    }

                    @Override
                    public void setWriteListener(WriteListener writeListener) {

                    }

                    @Override
                    public void write(int b) {
                        baos.write(b);
                    }
                };
            }
        };


        long start = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, wrappedResp);
        long end = System.currentTimeMillis() - start;

        byte[] bytes = baos.toByteArray();
        String responseStr = new String(bytes);
        servletResponse.getOutputStream().write(bytes);

        String requestBody = requestWrapper.getBody();
        String responseBody = responseStr;

        generateLog(requestWrapper, ((HttpServletResponse)servletResponse), requestBody, responseBody, end);
    }

//    public String getRequestBody(HttpServletRequest httpServletRequest, ByteArrayOutputStream baos) throws IOException {
//        ByteArrayOutputStream targetStream = new ByteArrayOutputStream();
//        IOUtils.copy(httpServletRequest.getInputStream(), targetStream);
//
//        byte[] bytes = baos.toByteArray();
//        String responseStr = new String(bytes);
//        targetStream.write(bytes);
//
//        return responseStr;
//    }

    public String getResponseBody(HttpServletResponse httpServletResponse, ByteArrayOutputStream baos) throws IOException {
        byte[] bytes = baos.toByteArray();
        String responseStr = new String(bytes);
        httpServletResponse.getOutputStream().write(bytes);
        return responseStr;
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
        log.setResponseTime(String.valueOf(time) + " ms\n");

        objectMapper.writeValue(System.out, log);
    }

    public static class ByteArrayPrintWriter extends PrintWriter {
        public ByteArrayPrintWriter(OutputStream out) {
            super(out);
        }
    }
}

