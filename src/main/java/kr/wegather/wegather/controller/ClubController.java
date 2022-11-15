package kr.wegather.wegather.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.auth.PrincipalDetails;
import kr.wegather.wegather.domain.*;
import kr.wegather.wegather.domain.enums.ClubRoleAuthLevel;
import kr.wegather.wegather.domain.enums.ClubRoleIsDefault;
import kr.wegather.wegather.domain.enums.QuestionnaireStatus;
import kr.wegather.wegather.function.Security;
import kr.wegather.wegather.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
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
	private final SelectionService selectionService;

	@ApiOperation(value = "동아리 상세 정보 조회")
	@GetMapping("/{club_id}/")
	public ResponseEntity<String> searchClub(@PathVariable("club_id") String id) {
		// Club 객체 조회
		Long clubId = Long.parseLong(Security.decrypt(id));
		Club club;
		try {
			club = clubService.findOneWithUser(clubId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		JSONObject res = new JSONObject();

		// ClubSchool 객체 조회
		List<ClubSchool> clubSchools = clubSchoolService.findByClub(clubId);
		JSONArray clubSchoolsJSON = new JSONArray();
		for (ClubSchool clubSchool: clubSchools) {
			clubSchoolsJSON.put(clubSchool.getSchool().toJSONObject());
		}

		try {
			res.put("club", club.toJSONObject());
			res.put("schools", clubSchoolsJSON);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		// 응답 객체 생성
		return ResponseEntity.status(HttpStatus.OK).body(res.toString());
	}

	@ApiOperation(value = "동아리 역할 목록 조회")
	@GetMapping("/{club_id}/role/")
	public ResponseEntity<String> searchClubRoles(@PathVariable("club_id") String id) {
		// 동아리 역할 조회
		Long clubId = Long.parseLong(Security.decrypt(id));
		List<ClubRole> clubRoles = clubRoleService.findByClub(clubId);

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
	@GetMapping("/{club_id}/member/")
	public ResponseEntity<String> searchClubMembers(@PathVariable("club_id") String id) {
		// 구성원 조회
		Long clubId = Long.parseLong(Security.decrypt(id));
		List<ClubMember> clubMembers = clubMemberService.findByClub(clubId);

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
	@GetMapping("/{club_id}/form/")
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
	@GetMapping("/{club_id}/recruitment/")
	public ResponseEntity<String> searchRecruitments(@PathVariable("club_id") String id) {
		Long clubId = Long.parseLong(Security.decrypt(id));
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
	public ResponseEntity<String> createClub(@RequestBody createClubRequest request){
		// 유저 데이터
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Long userId = principalDetails.getUser().getId();
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
			res.put("club_id", Security.encrypt(clubId.toString()));
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
	@PostMapping("/role/")
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
		Timestamp endTime = request.endTime;
		Long recruitmentId = recruitmentService.createRecruitment(id, title, description);
		Long selectionId = selectionService.createSelection(recruitmentId, endTime);

		JSONObject res = new JSONObject();
		try {
			res.put("recruitmentId", recruitmentId);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
	}

	@ApiOperation(value = "동아리 상세 정보 수정")
	@PutMapping("/")
	public ResponseEntity updateClub(@PathVariable("club_id") Long id, @RequestBody updateClubRequest request) {
		// 수정할 fields
		String phone = request.phone, name = request.name, introduction = request.introduction, avatar = request.avatar;
		clubService.updateClub(id, phone, name, introduction, avatar);
		return new ResponseEntity(HttpStatus.OK);
	}

	@Data
	public static class searchClubsFilter {
		private Boolean isMySchool;
		private String query;
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
		private Timestamp endTime;
	}

}
