package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.Questionnaire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionnaireRepository {
    private final EntityManager em;

    public Long save(Questionnaire questionnaire) {
        em.persist(questionnaire);
        return questionnaire.getId();
    }

    public Questionnaire findOne(Long id) {
        return em.find(Questionnaire.class, id);
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
