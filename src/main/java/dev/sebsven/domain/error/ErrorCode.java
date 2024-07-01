package dev.sebsven.domain.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    RESOURCE_NOT_FOUND("ResourceNotFound", "Resource not found"),
    BAD_ARGUMENT("BadArgument", "Bad argument"),
    INVALID_OPERATION("InvalidOperation", "Operation not allowed");

    private final String shortCode;

    private final String message;

    ErrorCode(String shortCode, String message) {
        this.shortCode = shortCode;
        this.message = message;
    }
}