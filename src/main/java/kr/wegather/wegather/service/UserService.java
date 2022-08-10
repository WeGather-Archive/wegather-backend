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

    /* User 조회 */
    // 단건 조회 - By Id
    public User findOne(Long id) {
        return userRepository.findOne(id);
    }

    /* 로그인 */
    public User login(String email, String password) {
        // 유저 검색
        User user = userRepository.findOneByEmail(email);
        if (user == null)
            throw new UserException(UserExceptionType.USER_NOT_FOUND);

        BCryptPasswordEncoder Bcrypt = new BCryptPasswordEncoder();
        if (!Bcrypt.matches(password, user.getPassword()))
            throw new UserException(UserExceptionType.WRONG_INPUT);

        // token 발급
//        JSONObject token = new JSONObject();
//        try {
//            token.put("id", user.getId());
//            token.put("nickname", user.getNickname());
//            token.put("name", user.getName());
//            token.put("avatar", user.getAvatar());
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
        return user;
    }

    /* User 수정 */
    // Minor Info 수정

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
    // Password 수정

    public void changePassword(Long id, String password, String newPassword) {
        User user = userRepository.findOne(id);
        if (user == null)
            throw new UserException(UserExceptionType.USER_NOT_FOUND);

        BCryptPasswordEncoder Bcrypt = new BCryptPasswordEncoder();
        if (!Bcrypt.matches(password, user.getPassword()))
            throw new UserException(UserExceptionType.WRONG_INPUT);

        // 비밀번호 암호화
        user.setPassword(Bcrypt.encode(newPassword));
    }
    // Email 수정 - 이메일 인증 로직 추가

    public void changeEmail(Long id, String email) {
        User user;
        try {
            user = userRepository.findOne(id);
        } catch (Exception e) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }

        user.setEmail(email);
    }
    /* User 삭제 */

    public void delete(Long id) {
        User user;
        try {
            user = userRepository.findOne(id);
        } catch (Exception e) {
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }
        user.setIsDeleted(true);
    }
    // 중복 검사

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
