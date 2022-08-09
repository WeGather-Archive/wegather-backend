package kr.wegather.wegather.controller;

import kr.wegather.wegather.domain.Questionnaire;
import kr.wegather.wegather.domain.enums.QuestionnaireStatus;
import kr.wegather.wegather.exception.ApplicationException;
import kr.wegather.wegather.exception.ApplicationExceptionType;
import kr.wegather.wegather.service.ApplicationService;
import kr.wegather.wegather.service.QuestionnaireService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/form")
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;
    private final ApplicationService applicationService;

    @GetMapping("/")
    public ResponseEntity<String> searchQuestionnaire(@RequestParam Long id) {
        Questionnaire questionnaire = questionnaireService.findOne(id);
        if (questionnaire == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        JSONObject res = new JSONObject();
        try {
            res.put("questionnaire", questionnaire.toJSONObject());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(res.toString());
    }

    @PostMapping("/{form_id}")
    public ResponseEntity createApplication(@PathVariable("form_id") Long questionnaireId, @RequestBody createApplicationRequest request) {
        Long applicantId = request.applicantId;
        List<Object> tempAnswer = request.answer;
        List<String> answer = new ArrayList<>();

        for (Object tmpAns: tempAnswer) {
            try {
                answer.add(new JSONObject(tmpAns.toString()).toString());
            } catch (JSONException e) {
                throw new ApplicationException(ApplicationExceptionType.WRONG_INPUT);
            }
        }

        Long applicationId = applicationService.createApplication(applicantId, questionnaireId, answer);

        JSONObject res = new JSONObject();
        try {
            res.put("application_id", applicationId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
    }

    @PutMapping("/{form_id}")
    public ResponseEntity updateQuestionnaire(@PathVariable("form_id") Long id, @RequestBody updateQuestionnaireRequest request) {
        String title = request.title;
        List<String> question = request.question;
        QuestionnaireStatus status = request.status;
        questionnaireService.updateQuestionnaire(id, title, question, status);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{form_id}")
    public ResponseEntity deleteQuestionnaire(@PathVariable("form_id") Long id) {
        questionnaireService.deleteQuestionnaire(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    static class updateQuestionnaireRequest {
        String title;
        List<String> question;
        QuestionnaireStatus status;
    }

    @Data
    static class createApplicationRequest {
        Long applicantId;
        List<Object> answer;
    }
}
