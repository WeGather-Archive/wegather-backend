package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.ClubSchool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubSchoolRepository {
    private final EntityManager em;

    public Long save(ClubSchool clubSchool) {
        em.persist(clubSchool);
        return clubSchool.getId();
    }

    public List<ClubSchool> findByClub(Long clubId) {
        return em.createQuery("SELECT cs FROM ClubSchool cs JOIN FETCH cs.school s WHERE cs.club.id = :club", ClubSchool.class)
                .setParameter("club", clubId)
                .getResultList();
    }
}
