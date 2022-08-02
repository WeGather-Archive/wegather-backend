package kr.wegather.wegather.controller;

import kr.wegather.wegather.domain.Applicant;
import kr.wegather.wegather.domain.Recruitment;
import kr.wegather.wegather.domain.Selection;
import kr.wegather.wegather.domain.User;
import kr.wegather.wegather.domain.enums.ApplicantStatus;
import kr.wegather.wegather.service.ApplicantService;
import kr.wegather.wegather.service.SelectionService;
import kr.wegather.wegather.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;
//    private final RecruitmentService recruitmentService;
    private final UserService userService;
    private final SelectionService selectionService;

    @PostMapping("/{recruitment_id}")
    public ResponseEntity createApplicant(@PathVariable("recruitment_id") Long recruitmentId, @RequestBody Long userId) {
        Recruitment recruitment;
        User user;
        Selection selection;

        // Form Data
        try {
//            recruitment = recruitmentService.findOne(recruitmentId);
            user = userService.findOne(userId);

//            selection = recruitment.getSelections().get(0);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // Fill Applicant
        Applicant applicant = new Applicant();
        applicant.setUser(user);
//        applicant.setRecruitment(recruitment);
//        applicant.setSelection(selection);
        applicant.setStatus(ApplicantStatus.PENDING);

        // Create Applicant
        applicantService.createApplicant(applicant);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{recruitment_id}")
    public ResponseEntity<List<Applicant>> searchApplicant(@PathVariable("recruitment_id") Long recruitmentId) {
        List<Applicant> applicants = applicantService.findByRecruitment(recruitmentId);

        return ResponseEntity.status(HttpStatus.OK).body(applicants);
    }


    @PutMapping("/{id}")
    public ResponseEntity updateApplicant(@PathVariable("id") Long id, @RequestBody updateApplicantRequest updateApplicantRequest) {
        Long selectionId;
        ApplicantStatus status;

        // Form Data
        try {
            selectionId = updateApplicantRequest.selection;
            status = updateApplicantRequest.status;

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // Update Applicant
        applicantService.updateApplicant(id, selectionId, status);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteApplicant(@PathVariable("id") Long id) {
        // 상태만 변경?
        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    static class updateApplicantRequest {
        Long selection;
        ApplicantStatus status;
    }
}
