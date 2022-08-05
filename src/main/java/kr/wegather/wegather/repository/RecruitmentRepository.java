package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.Recruitment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepository {
    private final EntityManager em;

    public Long save(Recruitment recruitment) {
        em.persist(recruitment);
        return recruitment.getId();
    }

    public Recruitment findOne(Long recruitmentId) {
        return em.find(Recruitment.class, recruitmentId);
    }

    public Recruitment findOneWithApplicant(Long id) {
        return em.createQuery("SELECT r FROM Recruitment r JOIN FETCH r.selections s JOIN FETCH r.clubRole cr WHERE r.id = :id", Recruitment.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<Recruitment> findByClub(Long clubId) {
        return em.createQuery("SELECT r FROM Recruitment r JOIN r.clubRole cr WHERE cr.club.id = :club", Recruitment.class)
                .setParameter("club", clubId)
                .getResultList();
    }

    public void deleteOne(Long recruitmentId) {
        Recruitment recruitment = em.find(Recruitment.class, recruitmentId);
        em.remove(recruitment);
        em.flush();
    }
}
