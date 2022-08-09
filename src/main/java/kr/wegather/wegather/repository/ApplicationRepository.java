package kr.wegather.wegather.repository;


import kr.wegather.wegather.domain.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationRepository {
    private final EntityManager em;

    public Long save(Application application) {
        em.persist(application);
        return application.getId();
    }

    public Application findOne(Long id) {
        return em.find(Application.class, id);
    }

    public List<Application> findByQuestionnaire(Long questionnaireId) {
        return em.createQuery("SELECT a FROM Application a WHERE a.questionnaire.id = :questionnaire", Application.class)
                .setParameter("questionnaire", questionnaireId)
                .getResultList();
    }

    public Application findOneByApplicantAndQuestionnaire(Long applicantId, Long questionnaireId) {
        return em.createQuery("SELECT a FROM Application a WHERE a.applicant.id = application AND a.questionnaire.id = :questionnaire", Application.class)
                .setParameter("applicant", applicantId)
                .setParameter("questionnaire", questionnaireId)
                .getSingleResult();
    }

    public void deleteOne(Long id) {
        Application application = em.find(Application.class, id);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(application);
        em.flush();
        transaction.commit();
    }
}
