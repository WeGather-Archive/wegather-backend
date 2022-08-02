package kr.wegather.wegather.repository;

import kr.wegather.wegather.domain.Recruitment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepository {

	private final EntityManager em;

	public Long save(Recruitment rec) {
		em.persist(rec);
		return rec.getId();
	}

	public Recruitment findOne(Long id) {
		return em.find(Recruitment.class, id);
	}

	// 해당 클럽의 모든 모집 조회
	public List<Recruitment> findAllByClub(Long clubId) {
		return em.createQuery("SELECT r FROM Recruitment r WHERE r.club = :clubId", Recruitment.class)
				.setParameter("clubId", clubId)
				.getResultList();
	}

}
