package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiModelProperty;
import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.service.ClubService;
import kr.wegather.wegather.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ClubService clubService;

    @GetMapping("/club")
    public ResponseEntity<String> searchMyClubs(@RequestParam("uid") Long userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authUser = (User) authentication.getPrincipal();

            // get 으로 넘어온 id 값과 유저인증정보(세션) 의 id 를 비교
            if (Objects.equals(authUser.getId(), userId)) {
                // 인증 성공일때만 로직 실행
                List<Club> clubs = clubService.findByUserClubMember(userId);
                JSONArray clubArray = new JSONArray();
                for (Club club : clubs) {
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
            // 인증 실패
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인이 필요합니다");
        }
    }

    @GetMapping("/club/pending")
    public ResponseEntity<String> searchClubPending(@RequestParam("uid") Long userId) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authUser = (User) authentication.getPrincipal();

            // get 으로 넘어온 id 값과 유저인증정보(세션) 의 id 를 비교
            if (Objects.equals(authUser.getId(), userId)) {
                // 인증 성공일때만 로직 실행
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
            // 인증 실패
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인이 필요합니다");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> searchUser(@PathVariable("id") Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authUser = (User) authentication.getPrincipal();
            // get 으로 넘어온 id 값과 유저인증정보(세션) 의 id 를 비교
            if (Objects.equals(authUser.getId(), id)) {
                // 인증 성공일때만 로직 실행
                User findUser = userService.findOne(id);
                if (findUser == null)
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
                JSONObject res = findUser.toJSONObjet();
                return ResponseEntity.status(HttpStatus.OK).body(res.toString());
            }
            // 인증 실패
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인이 필요합니다");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody updateUserRequest request) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authUser = (User) authentication.getPrincipal();

            // get 으로 넘어온 id 값과 유저인증정보(세션) 의 id 를 비교
            if (Objects.equals(authUser.getId(), id)) {
                // 인증 성공일때만 로직 실행
                String nickname = request.nickname, avatar = request.avatar, profile = request.profile, phone = request.phone;
                try {
                    userService.updateUser(id, nickname, avatar, profile, phone);
                } catch (Exception e) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }

                return new ResponseEntity(HttpStatus.OK);

            }
            // 인증 실패
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인이 필요합니다");
        }
    }

    @PatchMapping("/pwd/{id}")
    public ResponseEntity updatePassword(@PathVariable("id") Long id, @RequestBody updatePasswordRequest request) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authUser = (User) authentication.getPrincipal();

            // get 으로 넘어온 id 값과 유저인증정보(세션) 의 id 를 비교
            if (Objects.equals(authUser.getId(), id)) {
                // 인증 성공일때만 로직 실행
                String password = request.password, newPassword = request.newPassword;
                try {
                    userService.changePassword(id, password, newPassword);
                } catch (Exception e) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity(HttpStatus.OK);

            }
            // 인증 실패
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인이 필요합니다");
        }
    }
    @PatchMapping("/email/{id}")
    public ResponseEntity updateEmail(@PathVariable("id") Long id, @RequestBody updateEmailRequst request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authUser = (User) authentication.getPrincipal();

            // get 으로 넘어온 id 값과 유저인증정보(세션) 의 id 를 비교
            if (Objects.equals(authUser.getId(), id)) {
                // 인증 성공일때만 로직 실행
                String email = request.email;
                try {
                    userService.changeEmail(id, email);
                } catch (Exception e) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity(HttpStatus.OK);
            }
            // 인증 실패
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인이 필요합니다");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authUser = (User) authentication.getPrincipal();

            // get 으로 넘어온 id 값과 유저인증정보(세션) 의 id 를 비교
            if (Objects.equals(authUser.getId(), id)) {
                // 인증 성공일때만 로직 실행
                try {
                    userService.delete(id);
                } catch (Exception e) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity(HttpStatus.OK);
            }
            // 인증 실패
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인이 필요합니다");
        }
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
