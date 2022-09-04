package kr.wegather.wegather.auth;

import kr.wegather.wegather.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring Security가 /login.do 요청이 들어오면
 * 로그인이 완료되면 Security Session 을 생성한다. ( SecurityHolder )
 * Object Type => Authentication 타입 객체
 * Authentication 안에 User 정보가 있어야 함.
 * User Object Type => UserDetails Type 객체
 *
 * Security Session -> Authentication -> UserDetails(PrincipalDetails)
 */
//@SuppressWarnings("serial")
public class PrincipalDetails implements UserDetails {
    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    // spring security 의 UserDetails 상속
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority(
                user.getAuthLevel().name()
        ));
        return authList;
    }

    // user password
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 우리 서비스는 email 이 id
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 계정의 만료여부 리턴 (true : 만료 안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정의 잠금여부 리턴 (true : 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호의 만료여부 리턴 (true : 만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정의 활성여부 리턴 (true : 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
