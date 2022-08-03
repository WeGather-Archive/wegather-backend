package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.domain.*;
import kr.wegather.wegather.service.ClubService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {
	private final ClubService clubService;

	@ApiOperation(value = "동아리 전체 목록 조회")
	@GetMapping("/")
	public ResponseEntity<List<Club>> searchClubs() {
		List<Club> clubs = clubService.findAll();
//		현재 해당사항 없음
//		필터 구체화 후 구현
		return ResponseEntity.status(HttpStatus.OK).body(clubs);
	}

	@ApiOperation(value = "동아리 등록")
	@PostMapping("/")
	public ResponseEntity<Long> createClub(@RequestBody createClubRequest request){
		Club club = new Club();
		club.setName(request.getName());

		Long id = clubService.createClub(club);
		return ResponseEntity.status(HttpStatus.CREATED).body(id);
	}

	@ApiOperation(value = "동아리 상세 정보 조회")
	@GetMapping("/{club_id}")
	public ResponseEntity<Club> searchClub() {
		Club club = new Club();

		return ResponseEntity.status(HttpStatus.OK).body(club);
	}

	@ApiOperation(value = "동아리 상세 정보 수정")
	@PutMapping("/{club_id}")
	public ResponseEntity<Long> updateClub(@PathVariable("id") Long id, @RequestBody @Validated updateClubRequest request) {
		clubService.update(id, request.getName());
		Club findClub = clubService.findOne(id);
		return ResponseEntity.status(HttpStatus.OK).body(findClub.getId());
	}

	@ApiOperation(value = "구성원 전체 목록 조회")
	@GetMapping("/member")
	public ResponseEntity<List<ClubMember>> searchClubMembers() {
		List<ClubMember> clubMembers = new ArrayList<>();

		return ResponseEntity.status(HttpStatus.OK).body(clubMembers);
	}

	@ApiOperation(value = "동아리 역할 목록 조회")
	@GetMapping("/role")
	public ResponseEntity<List<ClubRole>> searchClubRoles() {
		List<ClubRole> clubRoles = new ArrayList<>();

		return ResponseEntity.status(HttpStatus.OK).body(clubRoles);
	}

	@ApiOperation(value = "동아리 역할 생성")
	@PostMapping("/role")
	public ResponseEntity createClubRole() {

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@ApiOperation(value = "동아리 지원자 목록 조회")
	@GetMapping("/applicant")
	public ResponseEntity<List<Applicant>> searchApplicants() {
		List<Applicant> applicants = new ArrayList<>();


		return ResponseEntity.status(HttpStatus.OK).body(applicants);
	}

	@ApiOperation(value = "이메일 발송")
	@PostMapping("/email")
	public ResponseEntity createEmail() {
		// 현재 해당 사항 없음
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@ApiOperation(value = "모집 폼 목록 조회")
	@GetMapping("/form")
	public ResponseEntity<List<Questionnaire>> searchQuestionnaires() {
		List<Questionnaire> questionnaires = new ArrayList<>();
		return ResponseEntity.status(HttpStatus.OK).body(questionnaires);
	}

	@ApiOperation(value = "모집 폼 생성")
	@PostMapping("/form")
	public ResponseEntity createQuestionnaire() {

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@Data
	static class updateClubRequest {
		private String name;
	}

	@Data
	static class createClubRequest {
		private String name;
	}

}
