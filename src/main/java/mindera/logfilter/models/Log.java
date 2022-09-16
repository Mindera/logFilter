//Copyright 2022 Mindera
//SPDX-License-Identifier: Apache-2.0

package mindera.logfilter.models;

public class Log {
    private static final String LABEL = "logFilter";
    private Response response;
    private String date;
    private String service;
    private String environment;
    private Request request;
    private String responseTime;

    public Log() {
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getLABEL() {
        return LABEL;
    }

    @Override
    public String toString() {
        return "Log{" +
                " label='" + LABEL + '\'' +
                ", response=" + response +
                ", date='" + date + '\'' +
                ", service='" + service + '\'' +
                ", environment='" + environment + '\'' +
                ", request=" + request +
                ", responseTime='" + responseTime + '\'' +
                '}';
    }
}



