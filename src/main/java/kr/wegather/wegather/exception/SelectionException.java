package kr.wegather.wegather.exception;

public class SelectionException extends BaseException {
    private BaseExceptionType exceptionType;

    public SelectionException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
