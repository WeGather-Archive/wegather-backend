package kr.wegather.wegather.controller;

import kr.wegather.wegather.domain.Applicant;
import kr.wegather.wegather.domain.Application;
import kr.wegather.wegather.domain.Questionnaire;
import kr.wegather.wegather.repository.ApplicationRepository;
import kr.wegather.wegather.service.ApplicantService;
import kr.wegather.wegather.service.ApplicationService;
import kr.wegather.wegather.service.QuestionnaireService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ApplicantService applicantService;
    private final QuestionnaireService questionnaireService;

    @PostMapping("/{questionnaire_id}")
    public ResponseEntity createApplication(@PathVariable("questionnaire_id") Long questionnaireId, @RequestBody createApplicationRequest request) {
        Long applicantId;
        ArrayList<String> answer;
        Applicant applicant;
        Questionnaire questionnaire;

        try {
            applicantId = request.applicantId;
            answer = request.answer;
            applicant = applicantService.findOne(applicantId);
            questionnaire = questionnaireService.findOne(questionnaireId);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Application application = new Application();
        application.setQuestionnaire(questionnaire);
        application.setApplicant(applicant);
        application.setAnswer(answer);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{questionnaire_id}")
    public ResponseEntity<List<Application>> searchApplication(@PathVariable("questionnaire_id") Long questionnaireId) {
        List<Application> applications = applicationService.findByQuestionnaire(questionnaireId);

        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateApplication(@PathVariable("id") Long id, @RequestBody ArrayList<String> answer) {
        applicationService.updateApplication(id, answer);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteApplication(@PathVariable("id") Long id) {
        applicationService.deleteApplication(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    static class createApplicationRequest {
        Long applicantId;
        ArrayList<String> answer;
    }
}
