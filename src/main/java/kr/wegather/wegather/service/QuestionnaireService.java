package kr.wegather.wegather.service;

import kr.wegather.wegather.domain.Questionnaire;
import kr.wegather.wegather.domain.Selection;
import kr.wegather.wegather.domain.enums.QuestionnaireStatus;
import kr.wegather.wegather.exception.QuestionnaireException;
import kr.wegather.wegather.exception.QuestionnaireExceptionType;
import kr.wegather.wegather.exception.SelectionException;
import kr.wegather.wegather.exception.SelectionExceptionType;
import kr.wegather.wegather.repository.QuestionnaireRepository;
import kr.wegather.wegather.repository.SelectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final SelectionRepository selectionRepository;

    /* Questionnaire 생성 */
    public Long createQuestionnaire(Questionnaire questionnaire) {
        Long selectionId = questionnaire.getSelection().getId();
        // 전형이 존재하는지 확인
        Selection selection = selectionRepository.findOne(selectionId);
        if (selection == null)
            throw new SelectionException(SelectionExceptionType.NOT_FOUND);

        // 해당 전형에 이미 질문지가 존재하는지 확인
        Questionnaire duplicatedQuestionnaire = null;
        try {
            duplicatedQuestionnaire = questionnaireRepository.findOneBySelection(selectionId);
        } catch (Exception e) {
            // 없다면 질문지 생성
            questionnaireRepository.save(questionnaire);
            return questionnaire.getId();
        }
        // 있다면 Exception 생성
        if (duplicatedQuestionnaire != null) {
            throw new QuestionnaireException(QuestionnaireExceptionType.ALREADY_EXIST);
        }
        return null;
    }

    /* Questionnaire 조회 */
    // 단건 조회 - Id
    public Questionnaire findOne(Long id) {
        return questionnaireRepository.findOne(id);
    }

    // 복수 조회 - Selection
    public Questionnaire findBySelection(Long selectionId) {
        return questionnaireRepository.findOneBySelection(selectionId);
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
