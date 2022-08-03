package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.ClubRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubRoleRepository {
    private final EntityManager em;

    public Long save(ClubRole clubRole) {
        em.persist(clubRole);
        return clubRole.getId();
    }

    public List<ClubRole> findByClub(Long clubId) {
        return em.createQuery("SELECT cr FROM ClubRole cr WHERE cr.club.id = :club", ClubRole.class)
                .setParameter("club", clubId)
                .getResultList();
    }
}
