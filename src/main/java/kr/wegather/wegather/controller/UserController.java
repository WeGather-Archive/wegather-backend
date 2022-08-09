package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiModelProperty;
import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
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

    @GetMapping("/{id}")
    public ResponseEntity<String> searchUser(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        if (user == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        JSONObject res = user.toJSONObjet();
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
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
        String password = request.password, newPassword = request.newPassword;
        try {
            userService.changePassword(id, password, newPassword);
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
        @ApiModelProperty(name = "email", example = "test@test.com")
        private String email;
        @ApiModelProperty(name = "password", example = "qwe123!@#")
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
        private String newPassword;
    }

    @Data
    static class updateEmailRequst {
        private String email;
    }
}
