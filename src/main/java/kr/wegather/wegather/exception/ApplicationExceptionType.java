package kr.wegather.wegather.exception;

import org.springframework.http.HttpStatus;

public enum ApplicationExceptionType implements BaseExceptionType {
    WRONG_INPUT(400, HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 지원서입니다."),
    ALREADY_EXIST(409, HttpStatus.CONFLICT, "이미 지원서가 존재합니다.");


    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    ApplicationExceptionType(int errorCode, HttpStatus http, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = http;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
