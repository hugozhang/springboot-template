package com.winning.hmap.portal.logger.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 功能描述: 请求打印
 * @author cpj
 * @version 1.0
 */
public class RequestInfo extends Request {
    /**
     * 响应结果
     */
    private Object result;
    /**
     * 耗时
     */
    private String timeCost;


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("result", result)
                .append("timeCost", timeCost)
                .toString();
    }

    public Object getResult() {
        return this.result;
    }
    public void setResult(Object result) {
        this.result = result;
    }
    public String getTimeCost() {
        return this.timeCost;
    }
    public void setTimeCost(String timeCost) {
        this.timeCost = timeCost;
    }
}