package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.SchoolDept;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SchoolDeptRepository {
    private final EntityManager em;

    public List<SchoolDept> findBySchool(Long schoolId) {
        return em.createQuery("SELECT s FROM SchoolDept s WHERE s.school.id = :id", SchoolDept.class)
                .setParameter("id", schoolId)
                .getResultList();
    }
}
