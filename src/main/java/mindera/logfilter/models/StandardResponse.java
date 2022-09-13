package mindera.logfilter.models;

import java.util.Map;

public class StandardResponse extends Response {

    private int statusCode;


    public StandardResponse(String body, Map<String, String> header, int statusCode) {
        super(body, header);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
