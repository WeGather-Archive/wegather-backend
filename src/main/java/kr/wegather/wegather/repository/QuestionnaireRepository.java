package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.Questionnaire;
import kr.wegather.wegather.exception.QuestionnaireException;
import kr.wegather.wegather.exception.QuestionnaireExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionnaireRepository {
    private final EntityManager em;

    public Long save(Questionnaire questionnaire) {
        try {
            em.persist(questionnaire);
        } catch (Exception e) {
            if (e.getClass() == PersistenceException.class) {
                throw new QuestionnaireException(QuestionnaireExceptionType.ALREADY_EXIST);
            }
            throw new QuestionnaireException(QuestionnaireExceptionType.WRONG_INPUT);
        }
        return questionnaire.getId();
    }

    public Questionnaire findOne(Long id) {
        return em.find(Questionnaire.class, id);
    }

    public List<Questionnaire> findByClub(Long clubId) {
        return em.createQuery("SELECT q FROM Questionnaire q JOIN q.selection s JOIN s.recruitment r JOIN r.clubRole cr WHERE cr.club.id = :club", Questionnaire.class)
                .setParameter("club", clubId)
                .getResultList();
    }

    public List<Questionnaire> findBySelection(Long selectionId) {
        return em.createQuery("SELECT q FROM Questionnaire q WHERE q.selection = :selection", Questionnaire.class)
                .setParameter("selection", selectionId)
                .getResultList();
    }

    public void deleteOne(Long id) {
        Questionnaire questionnaire = em.find(Questionnaire.class, id);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(questionnaire);
        em.flush();
        transaction.commit();
    }
}
