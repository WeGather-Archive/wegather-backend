package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.exception.UserException;
import kr.wegather.wegather.exception.UserExceptionType;
import kr.wegather.wegather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* 회원가입 */
    @Transactional
    public Long register(User user) {
        validateDuplicateUser(user); // 중복 회원 검증

        // 비밀번호 암호화
        BCryptPasswordEncoder Bcrypt = new BCryptPasswordEncoder();
        user.setPassword(Bcrypt.encode(user.getPassword()));
//        SHA256 sha256 = new SHA256();
//        try {
//            user.setPassword(sha256.encrypt(user.getPassword()));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }

        userRepository.save(user);

        return user.getId();
    }

    public String login(Map<String, String> userParam) {
        // 유저 검색
        List<User> userFound = userRepository.findOneByEmail(userParam.get("email"));
        if (userFound.size() < 1)
        {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }

        User user = userFound.get(0);
        BCryptPasswordEncoder Bcrypt = new BCryptPasswordEncoder();
        if (!Bcrypt.matches(userParam.get("password"), user.getPassword()))
            throw new UserException(UserExceptionType.WRONG_INPUT);
//        return jwtTokenProvider.createToken(user.getId(), user.getNickname(), user.getUsername(), user.getProfile());
        return "Created";
    }

    public User find(Long id) {
        User user = userRepository.findOne(id);
        if (user == null)
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        return user;
    }

    public void changePassword(Long id, String password) {
        User user = userRepository.findOne(id);
        if (user == null)
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        user.setPassword(password);
    }

    public Long update(User user) {
        User userFound = userRepository.findOne(user.getId());
        if (userFound == null)
            throw new UserException(UserExceptionType.USER_NOT_FOUND);

        if (user.getProfile() != null)
            userFound.setProfile(user.getProfile());
        if (user.getNickname() != null)
            userFound.setNickname(user.getNickname());
        if (user.getName() != null)
            userFound.setName(user.getName());
        return userFound.getId();
    }

    public void delete(Long id) {
        User user = userRepository.findOne(id);
        if (user.getIsDeleted() || user == null)
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        user.setIsDeleted(true);
    }

    private void validateDuplicateUser(User user) {
        // 파라미터 검사
        List<User> findUsers = userRepository.findOneByEmail(user.getEmail());
        if (findUsers.size() > 0) {
            throw new UserException(UserExceptionType.ALREADY_EXIST);
        }
    }
}
