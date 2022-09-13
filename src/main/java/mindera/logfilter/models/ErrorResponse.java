package mindera.logfilter.models;

import java.util.Map;

public class ErrorResponse extends Response {

    private String exceptionMessage;

    public ErrorResponse(String body, Map<String, String> header, String exceptionMessage) {
        super(body, header);
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
