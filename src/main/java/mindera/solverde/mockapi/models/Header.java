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


}
