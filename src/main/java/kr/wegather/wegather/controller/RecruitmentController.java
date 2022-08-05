package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.domain.Recruitment;
import kr.wegather.wegather.domain.enums.RecruitmentStatus;
import kr.wegather.wegather.service.RecruitmentService;
import kr.wegather.wegather.service.SelectionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruitment")
public class RecruitmentController {
    private final RecruitmentService recruitmentService;
    private final SelectionService selectionService;

    @ApiOperation(value = "모집 상세 (동아리 지원자 목록 조회)")
    @GetMapping("/")
    public ResponseEntity<String> searchRecruitment(@RequestParam Long id) {
        Recruitment recruitmentWithApplicant = recruitmentService.findOneWithApplicant(id);
        JSONObject res = recruitmentWithApplicant.toJSONObject();

        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }

    @ApiOperation(value = "모집 정보 수정")
    @PutMapping("/{recruitment_id}")
    public ResponseEntity updateRecruitment(@PathVariable("recruitment_id") Long id, @RequestBody updateRecruitmentRequest request) {
        String title = request.title, description = request.description;
        RecruitmentStatus status = request.status;

        recruitmentService.updateRecruitment(id, title, description, status);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "모집 삭제")
    @DeleteMapping("/{recruitment_id}")
    public ResponseEntity deleteRecruitment(@PathVariable("recruitment_id") Long id) {
        recruitmentService.deleteRecruitment(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "모집 내에 전형 생성")
    @PostMapping("/{recruitment_id}")
    public ResponseEntity createSelection(@PathVariable("recruitment_id") Long id) {
        Long selectionId = selectionService.createSelection(id);

        JSONObject res = new JSONObject();
        try {
            res.put("id", selectionId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
    }


    @Data
    static class updateRecruitmentRequest {
        private String title;
        private String description;
        private RecruitmentStatus status;
    }
}
