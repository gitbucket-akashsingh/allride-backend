package com.example.allride.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    private final HttpStatus status;

    public BaseException(
            String message,
            ErrorCode errorCode,
            HttpStatus status
    ) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
