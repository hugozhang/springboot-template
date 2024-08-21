package com.winning.hmap.portal.logger.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Request {

    /**
     * 请求ip
     */
    private String ip;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求方法
     */
    private String httpMethod;
    /**
     * 类方法
     */
    private String classMethod;
    /**
     * 请求参数
     */
    private Object requestParams;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("ip", ip)
                .append("url", url)
                .append("httpMethod", httpMethod)
                .append("classMethod", classMethod)
                .append("requestParams", requestParams)
                .toString();
    }

    public String getIp() {
        return this.ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getHttpMethod() {
        return this.httpMethod;
    }
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
    public String getClassMethod() {
        return this.classMethod;
    }
    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }
    public Object getRequestParams() {
        return this.requestParams;
    }
    public void setRequestParams(Object requestParams) {
        this.requestParams = requestParams;
    }
}