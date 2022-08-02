package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.domain.ClubMember;
import kr.wegather.wegather.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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

    public ClubMember findByClubAndUser(Long clubId, Long userId) {
        return em.createQuery("SELECT cm FROM ClubMember cm WHERE cm.club.id = :club AND cm.user.id = :user", ClubMember.class)
                .setParameter("club", clubId)
                .setParameter("user", userId)
                .getSingleResult();
    }
    public List<ClubMember> findByClub(Long clubId) {
        return em.createQuery("SELECT cm FROM ClubMember cm WHERE cm.club.id = :club", ClubMember.class)
                .setParameter("club", clubId)
                .getResultList();
    }
}
