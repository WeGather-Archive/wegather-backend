package kr.wegather.wegather.exception;

import org.springframework.http.HttpStatus;

public enum QuestionnaireExceptionType implements BaseExceptionType {
    WRONG_INPUT(400, HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    ALREADY_EXIST(409, HttpStatus.CONFLICT, "해당 전형에 모집 폼이 이미 존재합니다.");

    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    QuestionnaireExceptionType(int errorCode, HttpStatus http, String errorMessage) {
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
