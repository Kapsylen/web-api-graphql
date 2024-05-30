package dev.sebsven.domain.error;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}