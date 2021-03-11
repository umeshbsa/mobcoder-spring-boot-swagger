package com.mobcoder.exam.utils;

import com.mobcoder.exam.base.BaseResponse;
import com.mobcoder.exam.base.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class Validation {

    public static ResponseEntity<BaseResponse> getFieldValid(String msg) {
        ErrorResponse error = new ErrorResponse(msg, 100);
        BaseResponse responseEntity = new BaseResponse(error);
        return new ResponseEntity<BaseResponse>(responseEntity, HttpStatus.OK);
    }

    public static ResponseEntity<BaseResponse> getResponseValid(Map<String, Object> dataMap) {
        BaseResponse responseEntity = new BaseResponse(dataMap);
        return new ResponseEntity<BaseResponse>(responseEntity, HttpStatus.OK);
    }

    public static ResponseEntity<BaseResponse> getErrorValid(String msg, int code) {
        ErrorResponse error = new ErrorResponse(msg, code);
        BaseResponse responseEntity = new BaseResponse(error);
        return new ResponseEntity<BaseResponse>(responseEntity, HttpStatus.OK);
    }
}
