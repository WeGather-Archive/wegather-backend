package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.domain.Applicant;
import kr.wegather.wegather.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruitment")
public class RecruitmentController {
    private final ApplicantService applicantService;

    @ApiOperation(value = "동아리 지원자 목록 조회")
    @GetMapping("/")
    public ResponseEntity<String> searchApplicants(@RequestParam Long id) {
        List<Applicant> applicants = applicantService.findByRecruitment(id);
        JSONArray applicantsArray = new JSONArray();
        for (Applicant applicant: applicants) {
            applicantsArray.put(applicant.toJSONObjectForClub());
        }

        JSONObject res = new JSONObject();
        try {
            res.put("applicants", applicantsArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }
}
