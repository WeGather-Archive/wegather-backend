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

	public List<Club> findAll() {
		return em.createQuery("SELECT u FROM Club u", Club.class)
				.getResultList();
	}

	public List<Club> findByName(String name) {
		List<Club> result =  em.createQuery("SELECT c FROM Club c WHERE c.name = :name", Club.class)
				.setParameter("name", name)
				.getResultList();

		return result;
	}
}
