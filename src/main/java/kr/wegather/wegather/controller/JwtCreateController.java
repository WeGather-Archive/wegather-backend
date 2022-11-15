package kr.wegather.wegather.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.wegather.wegather.auth.JwtProperties;
import kr.wegather.wegather.auth.oauth.provider.GoogleUser;
import kr.wegather.wegather.auth.oauth.provider.OAuthUserInfo;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.domain.enums.AuthLevel;
import kr.wegather.wegather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.spi.IIORegistry;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;



@RestController
@RequiredArgsConstructor
public class JwtCreateController {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;


	@PostMapping("/oauth/jwt/google")
	public String jwtCreate(@RequestBody Map<String, Object> data) {
		System.out.println("jwt create 실행");
		System.out.println(data);
		System.out.println(data.get("credential"));

		// credential decode

		HttpTransport transport = new NetHttpTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				.setAudience(Collections.singletonList(data.get("clientId").toString()))
				.build();

		String providerId = null;
		String email = null;
		String name = null;
		String pictureUrl = null;

		try {
			GoogleIdToken idToken = verifier.verify(data.get("credential").toString());

			GoogleIdToken.Payload payload = idToken.getPayload();
			providerId = payload.getSubject();
			email = payload.getEmail();
			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			name = (String) payload.get("name");
			pictureUrl = (String) payload.get("picture");

			System.out.println(providerId);
			System.out.println(email);
			System.out.println(name);
			System.out.println(pictureUrl);

		} catch (GeneralSecurityException e) {
			System.out.println("error");
		} catch (IOException e) {
			System.out.println("error");
		}

//		OAuthUserInfo googleUser = new GoogleUser((Map<String, Object>) data.get(("credential")));

		User userEntity = null;
		try{
			userEntity = userRepository.findOneByGoogleProviderId(providerId);
		}
		catch (Exception e) {
			if(userEntity == null) {
				User userRequest = User.builder()
						.name(name)
						.password(bCryptPasswordEncoder.encode("foo"))
						.email(email)
						.provider("google")
						.providerId(providerId)
						.authLevel(AuthLevel.USER)
						.build();

				userEntity = userRepository.saveUser(userRequest);
			}
		}

		String jwtToken = Jwts.builder()
				.setSubject(userEntity.getName())
				.setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.claim("id", userEntity.getId())
				.claim("name", userEntity.getName())
				.signWith(SignatureAlgorithm.HS256, JwtProperties.secretKey)
				.compact();

		return jwtToken;

	}
}
