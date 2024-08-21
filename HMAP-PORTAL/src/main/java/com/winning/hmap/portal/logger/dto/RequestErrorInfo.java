package com.winning.hmap.portal.logger.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 功能描述: 请求异常打印
 *
 * @author cpj
 * @version 1.0
 */
public class RequestErrorInfo extends Request {

    /**
     * 描述：请求异常
     */
    private RuntimeException exception;

    /**
     * 获取 描述：请求异常
     */
    public RuntimeException getException() {
        return this.exception;
    }

    /**
     * 设置 描述：请求异常
     */
    public void setException(RuntimeException exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("exception", exception)
                .toString();
    }
}