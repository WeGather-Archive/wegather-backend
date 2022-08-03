package kr.wegather.wegather.exception;

public class ClubMemberException extends BaseException {
    private BaseExceptionType exceptionType;

    public ClubMemberException(BaseExceptionType exceptionType) { this.exceptionType = exceptionType; }

    @Override
    public BaseExceptionType getExceptionType() { return exceptionType; }
}
