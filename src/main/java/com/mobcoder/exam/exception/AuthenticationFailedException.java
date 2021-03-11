package com.mobcoder.exam.exception;

public class AuthenticationFailedException extends Exception {


    public AuthenticationFailedException(){
        super();
    }

    public AuthenticationFailedException(String message){
        super(message);
    }
}
