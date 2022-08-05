package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.Applicant;
import kr.wegather.wegather.domain.enums.ApplicantStatus;
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

    public Applicant findByRecruitmentAndUser(Long recruitmentId, Long userId) {
        return em.createQuery("SELECT a FROM Applicant a WHERE a.recruitment.id = :recruitment AND a.user.id = :user", Applicant.class)
                .setParameter("recruitment", recruitmentId)
                .setParameter("user", userId)
                .getSingleResult();
    }

    public Integer updateApplicantBySelectionAndStatus(Long selectionId, Long applicantId, ApplicantStatus status) {
        return em.createQuery("UPDATE Applicant a SET a.status = :status WHERE a.id = :id AND a.selection.id = :selection")
                .setParameter("status", status)
                .setParameter("id", applicantId)
                .setParameter("selection", selectionId)
                .executeUpdate();
    }
}
