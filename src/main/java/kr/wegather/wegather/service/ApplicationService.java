package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Applicant;
import kr.wegather.wegather.domain.Application;
import kr.wegather.wegather.domain.Questionnaire;
import kr.wegather.wegather.exception.ApplicationException;
import kr.wegather.wegather.exception.ApplicationExceptionType;
import kr.wegather.wegather.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    /* Application 생성 */
    public Long createApplication(Long applicantId, Long questionnaireId, List<String> answer) {
        Application duplicatedApplication = applicationRepository.findOneByApplicantAndQuestionnaire(applicantId, questionnaireId);
        if (duplicatedApplication != null)
            throw new ApplicationException(ApplicationExceptionType.ALREADY_EXIST);
        Applicant applicant = new Applicant();
        applicant.setId(applicantId);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(questionnaireId);

        Application application = new Application();
        application.setApplicant(applicant);
        application.setQuestionnaire(questionnaire);
        application.setAnswer(answer);
        applicationRepository.save(application);

        return application.getId();
    }

    /* Application 조회 */
    // 단건 조회 - By Id
    public Application findOne(Long id) {
        return applicationRepository.findOne(id);
    }

    // 복수 조회 - By Questionnaire
    public List<Application> findByQuestionnaire(Long questionnaireId) {
        return applicationRepository.findByQuestionnaire(questionnaireId);
    }

    /* Application 수정 */
    public void updateApplication(Long id, ArrayList<String> answer) {
        Application application = applicationRepository.findOne(id);
//        application.setAnswer(answer);
    }

    /* Application 삭제 */
    public void deleteApplication(Long id) {
        applicationRepository.deleteOne(id);
    }
}
