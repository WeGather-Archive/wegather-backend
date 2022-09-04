package kr.wegather.wegather.auth;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
