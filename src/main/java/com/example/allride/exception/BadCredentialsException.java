package com.example.allride.exception;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException(String message){
        super(message);
    }
}
