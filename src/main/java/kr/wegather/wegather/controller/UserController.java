package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiModelProperty;
import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.service.ClubService;
import kr.wegather.wegather.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ClubService clubService;

    @GetMapping("/{id}")
    public ResponseEntity<String> searchUser(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        if (user == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        JSONObject res = user.toJSONObjet();
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }

    @GetMapping("/club")
    public ResponseEntity<String> searchMyClubs(@RequestParam("uid") Long userId) {
        // userId: Parameter 말고 Token에서 가져오기
        List<Club> clubs = clubService.findByUserClubMember(userId);
        JSONArray clubArray = new JSONArray();

        for (Club club: clubs) {
            clubArray.put(club.toJSONObject());
        }

        JSONObject res = new JSONObject();
        try {
            res.put("clubs", clubArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }

    @GetMapping("/club/pending")
    public ResponseEntity<String> searchClubPending(@RequestParam("uid") Long userId) {
        // userId: Parameter 말고 Token에서 가져오기
        List<Club> clubs = clubService.findByUserApplicant(userId);
        JSONArray clubArray = new JSONArray();
        for (Club club: clubs) {
            clubArray.put(club.toJSONObject());
        }

        JSONObject res = new JSONObject();
        try {
            res.put("clubs", clubArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody signUpRequest signUpRequest) {
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

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
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

//        res.addCookie(cookie);
        return new ResponseEntity(HttpStatus.OK);
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
