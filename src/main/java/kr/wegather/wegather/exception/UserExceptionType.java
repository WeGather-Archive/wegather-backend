package kr.wegather.wegather.exception;

import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType {
    // 회원가입, 로그인 시 //
    WRONG_INPUT(400, HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다."),
    ALREADY_EXIST(409, HttpStatus.CONFLICT, "이미 존재하는 계정입니다.");


    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    UserExceptionType(int errorCode, HttpStatus http, String errorMessage) {
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
