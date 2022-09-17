package kr.wegather.wegather.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserRepository userRepository;

    @Value("${secret-key.jwt}")
    private String secretKey = JwtProperties.secretKey;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if (validateAuthorizationHeader(header)) {
            chain.doFilter(request, response);
            return;
        }

        // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
        // 내가 SecurityContext에 집적접근해서 세션을 만들때 자동으로 UserDetailsService에 있는 loadByUsername이 호출됨.
        Claims claims = null;
        try {
            claims = parseJwtToken(header);
        } catch (SignatureException e) {
//            System.out.println(e);
            chain.doFilter(request, response);
            return;
        }
        Long id = claims.get("id", Long.class);

        if (id != null) {
            User user = userRepository.findOne(id);

            // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
            // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                    null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                    principalDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private Claims parseJwtToken(String authorizationHeader) throws SignatureException {
        String token = extractToken(authorizationHeader);
        Claims c = new DefaultClaims();
        try {
            c = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return c;
        } catch (MalformedJwtException e) {
            return c;
        }
        return c;
    }

    private Boolean validateAuthorizationHeader(String header) {
        return (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX));
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(JwtProperties.TOKEN_PREFIX.length());
    }
}
