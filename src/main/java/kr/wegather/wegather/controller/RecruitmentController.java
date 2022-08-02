package kr.wegather.wegather.controller;

import kr.wegather.wegather.domain.Recruitment;
import kr.wegather.wegather.service.RecruitmentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecruitmentController {

	private final RecruitmentService recruitmentService;

	// recruitment 생성
	@PostMapping("/recruitment")
	public ResponseEntity createRecruitment(@RequestBody @Validated createRecruitmentRequest request){
		Recruitment recruitment = new Recruitment();
		recruitment.setTitle(request.title);
		recruitment.setTitle(request.description);

		recruitmentService.createRecruitment(recruitment);

		return ResponseEntity.status(HttpStatus.CREATED).body(recruitment.toString());
	}



	@Data
	static class createRecruitmentRequest {
		private String title;
		private String description;

	}

}
