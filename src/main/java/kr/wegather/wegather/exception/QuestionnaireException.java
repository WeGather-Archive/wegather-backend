package kr.wegather.wegather.exception;

public class QuestionnaireException extends BaseException {

    private BaseExceptionType exceptionType;

    public QuestionnaireException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
