package mindera.solverde.mockapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Request {
    private String body;
    private String method;
    private String originalURL;
    private String query;
    private Header header;

    @Override
    public String toString() {
        return "Request{" +
                "body='" + body + '\n' +
                ", method='" + method + '\n' +
                ", originalURL='" + originalURL + '\n' +
                ", query='" + query + '\n' +
                ", header=" + header +
                '}';
    }
}
