package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.School;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SchoolRepository {
    private final EntityManager em;

    public List<School> findByName(String name) {
        return em.createQuery("SELECT s FROM School s WHERE UPPER(s.name) LIKE CONCAT('%',UPPER(:name),'%')", School.class)
                .setParameter("name", name)
                .getResultList();
    }
}
