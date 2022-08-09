package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.Club;
import kr.wegather.wegather.domain.ClubMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClubRepository {
	private final EntityManager em;

	public Long save(Club club) {
		em.persist(club);
		return club.getId();
	}

	public Club findOne(Long id) {
		return em.find(Club.class, id);
	}

	public Club findOneWithUser(Long id) {
		return em.createQuery("SELECT c FROM Club c JOIN FETCH c.admin WHERE c.id = :id", Club.class)
				.setParameter("id", id)
				.getSingleResult();
	}

	public List<Club> findAll() {
		return em.createQuery("SELECT c FROM Club c", Club.class)
				.getResultList();
	}

	public List<Club> findByName(String name) {
		return em.createQuery("SELECT c FROM Club c WHERE c.name = :name", Club.class)
				.setParameter("name", name)
				.getResultList();
	}

    public List<Club> findByUserClubMember(Long userId) {
		return em.createQuery("SELECT c FROM ClubMember cm JOIN cm.club c WHERE cm.user.id = :user", Club.class)
				.setParameter("user", userId)
				.getResultList();
    }

	public List<Club> findByUserApplicant(Long userId) {
		return em.createQuery("SELECT c FROM Applicant a JOIN a.recruitment r JOIN r.clubRole cr JOIN cr.club c WHERE a.user.id = :user", Club.class)
				.setParameter("user", userId)
				.getResultList();
	}
}
