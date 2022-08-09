package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.domain.*;
import kr.wegather.wegather.domain.enums.ClubRoleAuthLevel;
import kr.wegather.wegather.domain.enums.ClubRoleIsDefault;
import kr.wegather.wegather.domain.enums.QuestionnaireStatus;
import kr.wegather.wegather.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {
	private final UserService userService;
	private final ClubService clubService;
	private final ClubRoleService clubRoleService;
	private final ClubMemberService clubMemberService;
	private final ClubSchoolService clubSchoolService;
	private final QuestionnaireService questionnaireService;
	private final RecruitmentService recruitmentService;

	@ApiOperation(value = "동아리 전체 목록 조회")
	@GetMapping("")
	public ResponseEntity<String> searchClubs() {
		List<Club> clubs = clubService.findAll();
		JSONArray clubArray = new JSONArray();
		for (Club club: clubs) {
			clubArray.put(club.toJSONObject());
		}
//		현재 해당사항 없음
//		필터 구체화 후 구현

		JSONObject res = new JSONObject();
		try {
			res.put("clubs", clubArray);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.status(HttpStatus.OK).body(res.toString());
	}

	@ApiOperation(value = "동아리 상세 정보 조회")
	@GetMapping("/")
	public ResponseEntity<String> searchClub(@RequestParam Long id) {
		// Club 객체 조회
//		Long clubId = Long.parseLong(id);
		Long clubId = id;
		Club club;
		try {
			club = clubService.findOneWithUser(clubId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		JSONObject res = club.toJSONObject();

		// ClubSchool 객체 조회
		List<ClubSchool> clubSchools = clubSchoolService.findByClub(clubId);
		JSONArray clubSchoolsJSON = new JSONArray();
		for (ClubSchool clubSchool: clubSchools) {
			clubSchoolsJSON.put(clubSchool.getSchool().toJSONObject());
		}

		try {
			res.put("school", clubSchoolsJSON);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		// 응답 객체 생성
		return ResponseEntity.status(HttpStatus.OK).body(res.toString());
	}

	@ApiOperation(value = "동아리 역할 목록 조회")
	@GetMapping("/role")
	public ResponseEntity<String> searchClubRoles(@RequestParam Long id) {
		// 동아리 역할 조회
		List<ClubRole> clubRoles = clubRoleService.findByClub(id);

		// 응답 객체 생성
		JSONArray clubRoleArray = new JSONArray();
		for (ClubRole clubRole: clubRoles) {
			clubRoleArray.put(clubRole.toJSONObject());
		}
		JSONObject res = new JSONObject();
		try {
			res.put("club_roles", clubRoleArray);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.status(HttpStatus.OK).body(res.toString());
	}

	@ApiOperation(value = "구성원 전체 목록 조회")
	@GetMapping("/member")
	public ResponseEntity<String> searchClubMembers(@RequestParam Long id) {
		// 구성원 조회
		List<ClubMember> clubMembers = clubMemberService.findByClub(id);

		// 응답 객체 생성
		JSONObject res = new JSONObject();
		JSONArray clubMembersArray = new JSONArray();
		for (ClubMember clubMember: clubMembers) {
			clubMembersArray.put(clubMember.toJSONObject());
		}
		try {
			res.put("club_members", clubMembersArray);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.status(HttpStatus.OK).body(res.toString());
	}

	@ApiOperation(value = "모집 폼 목록 조회")
	@GetMapping("/form")
	public ResponseEntity<String> searchQuestionnaires(@RequestParam Long id) {
		List<Questionnaire> questionnaires = questionnaireService.findByClub(id);
		JSONArray questionnaireArray = new JSONArray();
		for (Questionnaire questionnaire: questionnaires) {
			questionnaireArray.put(questionnaire.toJSONObject());
		}

		JSONObject res = new JSONObject();
		try {
			res.put("questionnaires", questionnaireArray);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.status(HttpStatus.OK).body(res.toString());
	}

	@ApiOperation(value = "동아리 내 전체 모집 목록 조회 API")
	@GetMapping("/recruitment")
	public ResponseEntity<String> searchRecruitments(@RequestParam("id") Long clubId) {

		List<Recruitment> recruitments = recruitmentService.findByClub(clubId);
		JSONArray recruitmentArray = new JSONArray();
		for (Recruitment recruitment: recruitments) {
			recruitmentArray.put(recruitment.toJSONObjectForClub());
		}

		JSONObject res = new JSONObject();
		try {
			res.put("recruitment", recruitmentArray);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.status(HttpStatus.OK).body(res.toString());
	}

	@ApiOperation(value = "동아리 등록")
	@PostMapping("/")
	public ResponseEntity<String> createClub(@RequestParam("uid") Long userId, @RequestBody createClubRequest request){
		// 유저 데이터
//		Long userId = 1L; // Token에서 가져오기
		User user = userService.findOne(userId);

		// 앞으로 쓸 더미 데이터 생성
		Club dummyClub = new Club();
		ClubMember dummyClubMember = new ClubMember();
		ClubRole dummyClubRole = new ClubRole();

		// 동아리 및 동아리 관리자(Admin) 지정
		Club club = new Club();
		club.setAdmin(user);
		club.setName(request.name);
		club.setIntroduction(request.introduction);
		club.setAvatar(request.avatar);
		club.setPhone(request.phone);
		Long clubId = clubService.createClub(club);
		dummyClub.setId(clubId);

		// 동아리 역할 생성
		ClubRole clubRole = new ClubRole();
		clubRole.setClub(dummyClub);
		clubRole.setRole("동아리장");
		clubRole.setAuthLevel(ClubRoleAuthLevel.OPERATOR);
		clubRole.setIsDefault(ClubRoleIsDefault.DEFAULT_OPERATOR);
		Long clubRoleId = clubRoleService.createClubRole(clubRole);
		dummyClubRole.setId(clubRoleId);

		clubRole = new ClubRole();
		clubRole.setClub(dummyClub);
		clubRole.setRole("동아리원");
		clubRole.setAuthLevel(ClubRoleAuthLevel.MEMBER);
		clubRole.setIsDefault(ClubRoleIsDefault.DEFAULT_MEMBER);
		clubRoleService.createClubRole(clubRole);

		// 동아리장 생성 및 역할 부여
		ClubMember clubMember = new ClubMember();
		clubMember.setClub(dummyClub);
		clubMember.setRole(dummyClubRole);
		clubMember.setUser(user);
		Long clubMemberId = clubMemberService.createClubMember(clubMember);
		dummyClubMember.setId(clubMemberId);

		// 동아리의 소속 학교 = 동아리장의 학교;
		ClubSchool clubSchool = new ClubSchool();
		clubSchool.setClub(dummyClub);
		clubSchool.setSchool(user.getSchoolDept().getSchool());
		clubSchoolService.createClubSchool(clubSchool);


		// 응답 객체 생성
		JSONObject res = new JSONObject();
		try {
			res.put("club_id", clubId);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
	}

	@ApiOperation(value = "이메일 발송")
	@PostMapping("/email")
	public ResponseEntity createEmail() {
//		현재 해당 사항 없음
//		wegather 도메인으로 email 보내는 법 찾아본 후 구현
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@ApiOperation(value = "동아리 역할 생성")
	@PostMapping("/role/{club_id}")
	public ResponseEntity createClubRole(@PathVariable("club_id") Long id, @RequestBody createClubRoleRequest request) {
		// 동아리 역할 생성
		Club club = new Club();
		club.setId(id);
		String role = request.role;
		ClubRoleAuthLevel authLevel = request.authLevel;

		ClubRole clubRole = new ClubRole();
		clubRole.setClub(club);
		clubRole.setRole(role);
		clubRole.setAuthLevel(authLevel);

		clubRoleService.createClubRole(clubRole);

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@ApiOperation(value = "모집 폼 생성")
	@PostMapping("/form")
	public ResponseEntity<String> createQuestionnaire(@RequestBody createQuestionnaireRequest request) {
		// 모집 폼 생성
		Long selectionId = request.selectionId;
		Selection selection = new Selection();
		selection.setId(selectionId);
		String title = request.title;
		List<String> array = new ArrayList<>();

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setSelection(selection);
		questionnaire.setTitle(title);
		questionnaire.setQuestion(array);
		questionnaire.setStatus(QuestionnaireStatus.CREATED);

		Long formId;
		formId = questionnaireService.createQuestionnaire(questionnaire);

		JSONObject res = new JSONObject();
		try {
			res.put("form_id", formId);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@ApiOperation(value = "모집 생성")
	@PostMapping("/recruitment/{club_role_id}")
	public ResponseEntity createRecruitment(@PathVariable("club_role_id") Long id, @RequestBody createRecruitmentRequest request) {
		String title = request.title, description = request.description;
		Long recruitmentId = recruitmentService.createRecruitment(id, title, description);

		JSONObject res = new JSONObject();
		try {
			res.put("recruitmentId", recruitmentId);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
	}

	@ApiOperation(value = "동아리 상세 정보 수정")
	@PutMapping("/{club_id}")
	public ResponseEntity updateClub(@PathVariable("club_id") Long id, @RequestBody updateClubRequest request) {
		// 수정할 fields
		String phone = request.phone, name = request.name, introduction = request.introduction, avatar = request.avatar;
		clubService.updateClub(id, phone, name, introduction, avatar);
		return new ResponseEntity(HttpStatus.OK);
	}

	@Data
	static class createClubRequest {

		private String name;
		private String introduction;
		private String avatar;
		private String phone;
	}

	@Data
	static class updateClubRequest {
		private String phone;
		private String name;
		private String introduction;
		private String avatar;
	}

	@Data
	static class createClubRoleRequest {
		private String role;
		private ClubRoleAuthLevel authLevel;
	}

	@Data
	static class createQuestionnaireRequest {
		private Long selectionId;
		private String title;
	}

	@Data
	static class createRecruitmentRequest {
		private String title;
		private String description;
	}
}
