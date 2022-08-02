package kr.wegather.wegather.controller;

import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody signUpRequest signUpRequest) {
        // Set User Info
        SchoolDept schoolDept = new SchoolDept();
        schoolDept.setId(signUpRequest.schoolDept);
        User newUser = new User();
        newUser.setSchoolDept(schoolDept);
        newUser.setName(signUpRequest.name);
        if (signUpRequest.nickName != null)
            newUser.setNickname(signUpRequest.nickName);
        else {
            newUser.setNickname(signUpRequest.name);
        }
        newUser.setEmail(signUpRequest.email);
        newUser.setPassword (signUpRequest.password);

        userService.register(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser.toString());
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody loginRequest request, HttpServletResponse res) {
        // Check req
        String email = request.email, password = request.password;
        if (email == null || password == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        // 맞다면 Token 발급
        String token;
        try {
            token = userService.login(email, password);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Cookie cookie = new Cookie(
                "token",
                token
        );

        cookie.setMaxAge(30 * 60 * 1000);

        res.addCookie(cookie);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> searchUser(@PathVariable("id") Long id) {
        User user = new User();
        try {
            user = userService.findOne(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody updateUserRequest request) {
        String nickname = request.nickname, avatar = request.avatar, profile = request.profile, phone = request.phone;
        try {
            userService.updateUser(id, nickname, avatar, profile, phone);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/pwd/{id}")
    public ResponseEntity updatePassword(@PathVariable("id") Long id, @RequestBody updatePasswordRequest request) {
        String password = request.password;
        try {
            userService.changePassword(id, password);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/email/{id}")
    public ResponseEntity updateEmail(@PathVariable("id") Long id, @RequestBody updateEmailRequst request) {
        String email = request.email;
        try {
            userService.changeEmail(id, email);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        try {
            userService.delete(id);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    static class loginRequest {
        private String email;
        private String password;
    }

    @Data
    static class signUpRequest {
        private Long schoolDept;
        private String name;
        private String nickName;
        private String email;
        private String password;
    }

    @Data
    static class updateUserRequest {
        private String nickname;
        private String avatar;
        private String profile;
        private String phone;
    }

    @Data
    static class updatePasswordRequest {
        private String password;
    }

    @Data
    static class updateEmailRequst {
        private String email;
    }
}
