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
        return em.createQuery("SELECT s FROM Selection s WHERE s.recruitment = :recruitment", Selection.class)
                .setParameter("recruitment", recruitmentId)
                .getResultList();
    }

    public void deleteOne(Long id) {
        Selection selection = em.find(Selection.class, id);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(selection);
        em.flush();
        transaction.commit();
    }
}
