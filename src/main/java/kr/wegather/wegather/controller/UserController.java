package kr.wegather.wegather.controller;

import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.security.JwtTokenProvider;
import kr.wegather.wegather.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest) {
        // Set User Info
        SchoolDept schoolDept = new SchoolDept();
        schoolDept.setId(signUpRequest.schoolDept);
        User newUser = new User();
        newUser.setSchoolDept(schoolDept);
        newUser.setName(signUpRequest.name);
        newUser.setEmail(signUpRequest.email);
        newUser.setPassword (signUpRequest.password);

        userService.register(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> user, HttpServletResponse res) {
        // Check if req is correct
        if (user.get("email").isBlank() || user.get("password").isBlank())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        String Token = userService.login(user);
        Cookie cookie = new Cookie(
                "token",
                Token
        );

        cookie.setPath("/");
        cookie.setMaxAge(30 * 60 * 1000);

        res.addCookie(cookie);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> searchUser(@PathVariable("id") Long id) {
        User user = userService.find(id);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updatePassword(@PathVariable("id") Long id, @RequestBody String password) {
        userService.changePassword(id, password);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @Data
    static class SignUpRequest {
        private Long schoolDept;
        private String name;
        private String email;
        private String password;
    }
}
