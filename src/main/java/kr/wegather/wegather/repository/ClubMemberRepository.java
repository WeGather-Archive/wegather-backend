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

    /* ClubMember 생성 */
    public Long save(ClubMember clubMember) {
        em.persist(clubMember);
        return clubMember.getId();
    }


    /* ClubMember 조회 */
    // 단건 조회 - By Id
    public ClubMember findOne(Long id) {
        ClubMember clubMember = em.find(ClubMember.class, id);
        if (clubMember.getIsDeleted()) {
            return null;
        } else {
            return clubMember;
        }
    }

    // 단건 조회 - By Club & User
    public ClubMember findByClubAndUser(Long clubId, Long userId) {
        return em.createQuery("SELECT cm FROM ClubMember cm WHERE cm.club.id = :club AND cm.user.id = :user", ClubMember.class)
                .setParameter("club", clubId)
                .setParameter("user", userId)
                .getSingleResult();
    }

    // 복수 조회 - By Club
    public List<ClubMember> findByClub(Long clubId) {

        return em.createQuery("SELECT cm FROM ClubMember cm JOIN FETCH cm.user u JOIN FETCH cm.role r WHERE cm.club.id = :club", ClubMember.class)
                .setParameter("club", clubId)
                .getResultList();
    }


    /* ClubMember 수정 */
    // Single Update - By Id


    // Bulk Update - By ClubRole
    public Integer updateClubMembersByClubRole(Long roleId, Long newRoleId) {
        Integer resultCount = em.createQuery("UPDATE ClubMember m SET m.role.id = :newRole WHERE m.role.id = :role")
                .setParameter("newRole", newRoleId)
                .setParameter("role", roleId)
                .executeUpdate();
        return resultCount;
    }


    /* ClubMember 삭제 */
}
