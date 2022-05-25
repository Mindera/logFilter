package mindera.solverde.mockapi.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Log {
    private Response response;
    private String date;
    private String service;
    private String environment;
    private Request request;
    private String responseTime;

    @Override
    public String toString() {
        return "Log{" +
                "response=" + response +
                ", date='" + date + '\'' +
                ", service='" + service + '\'' +
                ", environment='" + environment + '\'' +
                ", request=" + request +
                ", responseTime='" + responseTime + '\'' +
                '}';
    }
}




