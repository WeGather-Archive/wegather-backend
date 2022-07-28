package kr.wegather.wegather.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.wegather.wegather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private String secretKey = "Let's go WeGather!";

    // 토큰 유효시간 30분
    private Long tokenValidTime = 30 * 60 * 1000L;
    private final UserRepository userRepository;

    // 객체 초기화, secretKey 를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT Token 생성
    public String createToken(Long id, String nickname, String username, String profile) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        claims.put("nickname", nickname);
        claims.put("username", username);
        claims.put("profile", profile);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 토큰에 담길 정보
                .setIssuedAt(now) // 토큰 발행 시각
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 토큰 만료 시각
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘과 signature에 들어갈 secretKey
                .compact();
    }

//    public String createToken(Long userId, List<String> roles) {
//        Claims claims = Jwts.claims().setSubject(userId.toString());
//        claims.put("roles", roles);
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims) // 토큰에 담길 정보
//                .setIssuedAt(now) // 토큰 발행 시각
//                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 토큰 만료 시각
//                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘과 signature에 들어갈 secretKey
//                .compact();
//    }


    // JWT Token 에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userRepository.findOne(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Token 에서 정보 추출
    public Long getUserId(String token) {
        String userId = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return Long.parseLong(userId);
    }
    // Request Header 의 "Authorization"에 담긴 Token 가져오기 "key": "value"

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
    // 토큰의 유효성 + 만료일자 확인

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
