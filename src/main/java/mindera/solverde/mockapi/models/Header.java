package mindera.solverde.mockapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Header {

    private String contentLength;
    private String userAgent;
    private String acceptEncoding;
    private String accept;
    private String host;
    private String connection;
    private String ContentType;

    @Override
    public String toString() {
        return "Header{" +
                "contentLength='" + contentLength + '\n' +
                ", userAgent='" + userAgent + '\n' +
                ", acceptEncoding='" + acceptEncoding + '\'' +
                ", accept='" + accept + '\n' +
                ", host='" + host + '\n' +
                ", connection='" + connection + '\n' +
                ", ContentType='" + ContentType + '\n' +
                '}';
    }
}
