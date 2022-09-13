package kr.wegather.wegather.auth;

import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.domain.enums.AuthLevel;
import kr.wegather.wegather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	@Autowired private UserRepository userRepository;
	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		String provider = userRequest.getClientRegistration().getRegistrationId(); // google
		String providerId = oAuth2User.getAttribute("sub");
		String username = provider + "_" + providerId;

		String uuid = UUID.randomUUID().toString().substring(0,6);
		String password = bCryptPasswordEncoder.encode("패스워드" + uuid);

		String email = oAuth2User.getAttribute("email");
		AuthLevel authLevel = AuthLevel.USER;

		User byUsername = userRepository.findOneByName(username);

		// DB 에 OAuth2 유저가 없는 경우에는 회원가입 처리
		if(byUsername == null) {
			byUsername = User.oauth2Register()
					.name(username)
					.password(password)
					.email(email)
					.authLevel(authLevel)
					.provider(provider)
					.providerId(providerId)
					.build();

			userRepository.save(byUsername);
		}

		return new PrincipalDetails(byUsername, oAuth2User.getAttributes());


	}



}
