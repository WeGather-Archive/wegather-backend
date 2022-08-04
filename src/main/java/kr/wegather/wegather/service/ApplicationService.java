package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Application;
import kr.wegather.wegather.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
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
    public Long createApplication(Application application) {
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
        application.setAnswer(answer);
    }

    /* Application 삭제 */
    public void deleteApplication(Long id) {
        applicationRepository.deleteOne(id);
    }
}
