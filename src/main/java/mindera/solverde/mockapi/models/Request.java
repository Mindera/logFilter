package mindera.solverde.mockapi.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Data
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

}

