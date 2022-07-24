package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* 회원가입 */
    @Transactional
    public Long register(User user) {
        validateDuplicateUser(user); // 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        // 파라미터 검사
        List<User> findUsers = userRepository.findByEmail(user.getEmail());
        if (findUsers.size() > 0) {
            throw new IllegalStateException("User Already Exists");
        }
    }

    /* 회원 조회 */
    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }

}
