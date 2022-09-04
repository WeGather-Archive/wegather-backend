package kr.wegather.wegather.auth;

public interface JwtProperties {
    String secretKey = "2Ml0oN4bPAAV32tHOIfiiiB2ghz";
    Integer EXPIRATION_TIME = 864000000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
