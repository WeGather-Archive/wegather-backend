package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.domain.SchoolDept;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	AuthService authService;

	// 회원가입

	@ApiOperation(value = "회원가입")
	@PostMapping("/join")
	public ResponseEntity<User> join(@RequestBody signUpRequest signUpRequest) {
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

		authService.joinUser(newUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

	@GetMapping("/auth/user")
	public ResponseEntity<String> getAuthUser(Principal principal){
		System.out.println(principal.getName());
		return ResponseEntity.status(HttpStatus.OK).body(principal.getName());
	}

	// login 성공 시, 클라이언트에 현재 user 의 id 값 리턴
	@ApiIgnore
	@GetMapping("/login/success")
	public ResponseEntity<Long> loginSuccess(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal();
		System.out.println(user.getId());
		return ResponseEntity.status(HttpStatus.OK).body(user.getId());
	}


	@ApiIgnore
	@GetMapping("/auth/fail")
	public ResponseEntity loginFail(){
		return new ResponseEntity("로그인 실패",HttpStatus.UNAUTHORIZED);
	}


	@Data
	static class signUpRequest {
		private Long schoolDept;
		private String name;
		private String nickName;
		private String email;
		private String password;
	}
}
