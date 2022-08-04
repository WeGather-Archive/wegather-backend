package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.ClubRole;
import kr.wegather.wegather.domain.Selection;
import kr.wegather.wegather.domain.enums.ClubRoleIsDefault;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubRoleRepository {
    private final EntityManager em;

    /* ClubRole 생성 */
    public Long save(ClubRole clubRole) {
        em.persist(clubRole);
        return clubRole.getId();
    }


    /* ClubRole 조회 */
    // 단건 조회 - By Id
    public ClubRole findOne(Long roleId) {
        return em.find(ClubRole.class, roleId);
    }

    // 단건 조회 - By Club & isDefault
    public ClubRole findOneByClubAndIsDefault(Long clubId, ClubRoleIsDefault isDefault) {
        if (isDefault != ClubRoleIsDefault.CUSTOM) {
            return em.createQuery("SELECT cr FROM ClubRole cr WHERE cr.club.id = :id AND cr.isDefault = :isDefault", ClubRole.class)
                    .setParameter("id", clubId)
                    .setParameter("isDefault", isDefault)
                    .getSingleResult();
        }
        return null;
    }

    // 복수 조회 - By Club
    public List<ClubRole> findByClub(Long clubId) {
        return em.createQuery("SELECT cr FROM ClubRole cr WHERE cr.club.id = :club", ClubRole.class)
                .setParameter("club", clubId)
                .getResultList();
    }


    /* ClubRole 수정 */



    /* ClubRole 삭제 */
    public void deleteOne(Long roleId) {
        ClubRole clubRole = em.find(ClubRole.class, roleId);
        em.remove(clubRole);
        em.flush();
    }
}
