package mindera.solverde.mockapi.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class Request {
    private String body;
    private String method;
    private String originalURL;
    private String query;
    private Header header;

    public Request(String body, String method, String originalURL, String query, Header header) {
        this.body = body;
        this.method = method;
        this.originalURL = originalURL;
        this.query = query;
        this.header = header;
    }


    public String getHeader() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(header);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

