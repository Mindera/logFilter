package mindera.solverde.logfilter.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import mindera.solverde.logfilter.models.Log;
import mindera.solverde.logfilter.models.Request;
import mindera.solverde.logfilter.models.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
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

/*    @Override
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

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteArrayPrintWriter pw = new ByteArrayPrintWriter(baos);

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String requestBody = getRequestBody(httpRequest);

        HttpServletResponse wrappedResp = new HttpServletResponseWrapper(httpResponse) {
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
        filterChain.doFilter(servletRequest, wrappedResp);
        long end = System.currentTimeMillis() - start;

        byte[] bytes = baos.toByteArray();
        String responseStr = new String(bytes);
        servletResponse.getOutputStream().write(bytes);

        generateLog(httpRequest, wrappedResp, requestBody, responseStr, end);
    }

    public String getRequestBody(HttpServletRequest httpRequest) throws IOException {
        BufferedReader reader = httpRequest.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
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
