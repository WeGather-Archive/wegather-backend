package kr.wegather.wegather.exception;

import org.springframework.http.HttpStatus;

public enum ClubRoleExceptionType implements BaseExceptionType {
    WRONG_INPUT(400, HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "ClubRole이 존재하지 않습니다."),
    ALREADY_EXIST(409, HttpStatus.CONFLICT, "이미 존재하는 ClubRole입니다.");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    ClubRoleExceptionType(int errorCode, HttpStatus http, String errorMessage) {
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
