package kr.wegather.wegather.config;

import kr.wegather.wegather.handler.CustomAuthenticationFailureHandler;
import kr.wegather.wegather.handler.CustomAuthenticationSuccessHandler;
import kr.wegather.wegather.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
//@Order(0)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final AuthService authService;

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
					.antMatchers("/user/**", "/login/**").authenticated() // /user/** 만 로그인 필요
				.anyRequest().permitAll() // 나머지는 허용
					.and()
				.formLogin()
				.usernameParameter("email")
				.passwordParameter("password")
				.loginProcessingUrl("/auth/login")
				.defaultSuccessUrl("/login/success")
				.failureUrl("/auth/fail")
//				.successHandler(customAuthenticationSuccessHandler)
//				.failureHandler(customAuthenticationFailureHandler)
				.and()
				.logout()
				.invalidateHttpSession(true) // 로그아웃 시 세션초기화
				.deleteCookies("JSESSIONID");
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authService).passwordEncoder(new BCryptPasswordEncoder());
	}
}


