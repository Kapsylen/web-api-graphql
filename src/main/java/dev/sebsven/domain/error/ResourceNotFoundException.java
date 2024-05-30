package dev.sebsven.domain.error;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode,message);
    }
}