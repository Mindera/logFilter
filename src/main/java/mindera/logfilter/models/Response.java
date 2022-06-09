//Copyright 2022 Mindera
//SPDX-License-Identifier: Apache-2.0

package mindera.logfilter.models;

public class Response {

    private String body;

    public Response(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}


