package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Questionnaire;
import kr.wegather.wegather.domain.enums.QuestionnaireStatus;
import kr.wegather.wegather.repository.QuestionnaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;

    /* Questionnaire 생성 */
    public Long createQuestionnaire(Questionnaire questionnaire) {
        questionnaireRepository.save(questionnaire);
        return questionnaire.getId();
    }

    /* Questionnaire 조회 */
    // 단건 조회 - Id
    public Questionnaire findOne(Long id) {
        return questionnaireRepository.findOne(id);
    }

    // 복수 조회 - Selection
    public List<Questionnaire> findBySelection(Long selectionId) {
        return questionnaireRepository.findBySelection(selectionId);
    }

    /* Questionnaire 수정 */
    public void updateQuestionnaire(Long id, String title, List<String> question, QuestionnaireStatus status) {
        Questionnaire questionnaire = questionnaireRepository.findOne(id);
        questionnaire.setTitle(title);
        questionnaire.setQuestion(question);
        questionnaire.setStatus(status);
    }

    /* Questionnaire 삭제 */
    public void deleteQuestionnaire(Long id) {
        questionnaireRepository.deleteOne(id);
    }

    public List<Questionnaire> findByClub(Long clubId) {
        return questionnaireRepository.findByClub(clubId);
    }
}
