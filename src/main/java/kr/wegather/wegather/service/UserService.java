package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.exception.UserException;
import kr.wegather.wegather.exception.UserExceptionType;
import kr.wegather.wegather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* 회원가입 */
    @Transactional
    public Long register(User user) {
        String email = user.getEmail();
        validateDuplicateUser(email); // 중복 회원 검증

        // 비밀번호 암호화
        BCryptPasswordEncoder Bcrypt = new BCryptPasswordEncoder();
        user.setPassword(Bcrypt.encode(user.getPassword()));

        userRepository.save(user);

        return user.getId();
    }

    public String login(String email, String password) {
        // 유저 검색
        User user;
        try {
            user = userRepository.findOneByEmail(email);
        } catch (Exception e) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }

        BCryptPasswordEncoder Bcrypt = new BCryptPasswordEncoder();
        if (!Bcrypt.matches(password, user.getPassword()))
            throw new UserException(UserExceptionType.WRONG_INPUT);

        // token 발급
        JSONObject token = new JSONObject();
        try {
            token.put("id", user.getId());
            token.put("nickname", user.getNickname());
            token.put("name", user.getName());
            token.put("avatar", user.getAvatar());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return token.toString();
    }

    public User findOne(Long id) {
        User user;
        try {
            user = userRepository.findOne(id);
        } catch (Exception e) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }
        return user;
    }

    public void updateUser(Long id, String nickname, String avatar, String profile, String phone) {
        User user;
        try {
            user = userRepository.findOne(id);
        } catch (Exception e) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }

        user.setNickname(nickname);
        user.setAvatar(avatar);
        user.setProfile(profile);
        user.setPhone(phone);
    }

    public void changePassword(Long id, String password) {
        User user;
        try {
            user = userRepository.findOne(id);
        } catch (Exception e) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }

        // 비밀번호 암호화
        BCryptPasswordEncoder Bcrypt = new BCryptPasswordEncoder();
        user.setPassword(Bcrypt.encode(password));
    }

    public void changeEmail(Long id, String email) {
        User user;
        try {
            user = userRepository.findOne(id);
        } catch (Exception e) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }

        user.setEmail(email);
    }

    public void delete(Long id) {
        User user;
        try {
            user = userRepository.findOne(id);
        } catch (Exception e) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }
        user.setIsDeleted(true);
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
}
