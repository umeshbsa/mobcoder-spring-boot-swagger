package com.mobcoder.exam.base;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse implements Serializable {
    public String errorMsg;
    public int errorCode;

    public ErrorResponse(String errorMsg, int errorCode) {
        super();
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }
}
