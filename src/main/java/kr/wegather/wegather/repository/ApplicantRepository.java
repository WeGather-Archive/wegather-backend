package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.Applicant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicantRepository {
    private final EntityManager em;

    public Long save(Applicant applicant) {
        em.persist(applicant);
        return applicant.getId();
    }

    public Applicant findOne(Long id) {
        return em.find(Applicant.class, id);
    }

    public List<Applicant> findByRecruitment(Long recruitmentId) {
        return em.createQuery("SELECT a FROM Applicant a WHERE a.recruitment = :recruitment", Applicant.class)
                .setParameter("recruitment", recruitmentId)
                .getResultList();
    }

    public List<Applicant> findBySelection(Long selectionId) {
        return em.createQuery("SELECT a FROM Applicant a WHERE a.selection = :selection", Applicant.class)
                .setParameter("selection", selectionId)
                .getResultList();
    }
}
