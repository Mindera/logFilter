package mindera.logfilter.models;

import java.util.Map;


public class Request {

    private String body;
    private String method;
    private String originalURL;
    private String query;
    private Map<String, String> header;

    public Request(String body, String method, String originalURL, String query, Map<String, String> header) {
        this.body = body;
        this.method = method;
        this.originalURL = originalURL;
        this.query = query;
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public String getMethod() {
        return method;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
}


