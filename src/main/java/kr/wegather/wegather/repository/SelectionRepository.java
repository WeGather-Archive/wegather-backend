package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.Selection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SelectionRepository {
    private final EntityManager em;

    public Long save(Selection selection) {
        em.persist(selection);
        return selection.getId();
    }

    public Selection findOne(Long id) {
        return em.find(Selection.class, id);
    }

    public List<Selection> findByRecruitment(Long recruitmentId) {
        return em.createQuery("SELECT s FROM Selection s WHERE s.recruitment.id = :recruitment", Selection.class)
                .setParameter("recruitment", recruitmentId)
                .getResultList();
    }

    public Selection findOneWithUser(Long id) {
        return em.createQuery("SELECT s FROM Selection s LEFT JOIN FETCH s.applicants a JOIN FETCH a.user u JOIN FETCH u.schoolDept sd JOIN FETCH sd.school WHERE s.id = :id", Selection.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public Selection findByRecruitmentAndOrder(Long recruitmentId, Integer order) {
        return em.createQuery("SELECT s FROM Selection s WHERE s.recruitment.id = :recruitment AND s.order = :order", Selection.class)
                .setParameter("recruitment", recruitmentId)
                .setParameter("order", order)
                .getSingleResult();
    }

    public Integer updateSelectionByRecruitmentAndOrder(Long recruitmentId, Integer order) {
        return em.createQuery("UPDATE Selection s SET s.order = s.order - 1 WHERE s.recruitment.id = :recruitment AND s.order > :order")
                .setParameter("recruitment", recruitmentId)
                .setParameter("order", order)
                .executeUpdate();
    }

    public void deleteOne(Long id) {
        Selection selection = em.find(Selection.class, id);
        em.remove(selection);
    }
}
