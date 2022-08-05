package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;

    @ApiOperation(value = "특정 모집 지원하기")
    @PostMapping("/{recruitment_id}")
    public ResponseEntity<String> createApplicant(@PathVariable("recruitment_id") Long id) {
        Long userId = 5L;
        Long applicantId = applicantService.createApplicant(id, userId);

        JSONObject res = new JSONObject();
        try {
            res.put("id", applicantId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
    }
}
