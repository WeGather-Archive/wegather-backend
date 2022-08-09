package kr.wegather.wegather.exception;

public class ApplicationException extends BaseException {
    private BaseExceptionType exceptionType;

    public ApplicationException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }

}
