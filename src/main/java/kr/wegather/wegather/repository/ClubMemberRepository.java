package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.ClubMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubMemberRepository {
    private final EntityManager em;

    public Long save(ClubMember clubMember) {
        em.persist(clubMember);
        return clubMember.getId();
    }

    public ClubMember findOne(Long id) {
        ClubMember clubMember = em.find(ClubMember.class, id);
        if (clubMember.getIsDeleted()) {
            return null;
        } else {
            return clubMember;
        }
    }

    public List<ClubMember> findByClub(Long clubId) {

        return em.createQuery("SELECT cm FROM ClubMember cm JOIN FETCH cm.user u JOIN FETCH cm.role r WHERE cm.club.id = :club", ClubMember.class)
                .setParameter("club", clubId)
                .getResultList();
    }

    public ClubMember findByClubAndUser(Long clubId, Long userId) {
        return em.createQuery("SELECT cm FROM ClubMember cm WHERE cm.club.id = :club AND cm.user.id = :user", ClubMember.class)
                .setParameter("club", clubId)
                .setParameter("user", userId)
                .getSingleResult();
    }
}
