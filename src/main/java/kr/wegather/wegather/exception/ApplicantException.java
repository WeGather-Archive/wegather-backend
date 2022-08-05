package kr.wegather.wegather.exception;

public class ApplicantException extends BaseException {
    private BaseExceptionType exceptionType;

    public ApplicantException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
