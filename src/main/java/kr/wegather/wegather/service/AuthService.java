package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.domain.enums.AuthLevel;
import kr.wegather.wegather.exception.UserException;
import kr.wegather.wegather.exception.UserExceptionType;
import kr.wegather.wegather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

	private final UserRepository userRepository;

	@Transactional
	public Long joinUser(User user){
		String email = user.getEmail();
		validateDuplicateUser(email); // 중복 회원 검증

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		userRepository.save(user);

		return user.getId();
	}

	private void validateDuplicateUser(String email) {
		// 파라미터 검사
		try {
			User user = userRepository.findOneByEmail(email);
		} catch (Exception e) {
			return;
		}
		throw new UserException(UserExceptionType.ALREADY_EXIST);
	}


	// 여기서의 Username 은 email 을 뜻함
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findOneByEmail(email);

		if(user == null) throw new UsernameNotFoundException("Not found user");

		return user;
	}
}
