//Copyright 2022 Mindera
//SPDX-License-Identifier: Apache-2.0

package mindera.logfilter.models;

import java.util.Map;

public abstract class Response {

    private String body;
    private Map<String, String> header;

    public Response(String body, Map<String, String> header) {
        this.body = body;
        this.header = header;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}


