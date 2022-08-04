package kr.wegather.wegather.exception;

public class ClubRoleException extends BaseException{
    private BaseExceptionType exceptionType;

    public ClubRoleException(BaseExceptionType exceptionType) { this.exceptionType = exceptionType; }

    @Override
    public BaseExceptionType getExceptionType() { return exceptionType; }
}
